package de.fhbielefeld.newsboard.rest;

import de.fhbielefeld.newsboard.model.RawDocument;
import de.fhbielefeld.newsboard.model.access.Access;
import de.fhbielefeld.newsboard.model.access.AccessDao;
import de.fhbielefeld.newsboard.model.document.*;
import de.fhbielefeld.newsboard.model.module.ExternalModule;
import de.fhbielefeld.newsboard.model.module.ExternalModuleDao;
import de.fhbielefeld.newsboard.model.module.ModuleId;
import de.fhbielefeld.newsboard.processing.RawDocumentProcessor;
import de.fhbielefeld.newsboard.xml.ClassificationParsedHandler;
import de.fhbielefeld.newsboard.xml.XmlDocumentReader;
import de.fhbielefeld.newsboard.xml.XmlDocumentWriter;
import de.fhbielefeld.newsboard.xml.XmlException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.stream.XMLStreamException;
import java.io.StringReader;
import java.nio.charset.Charset;
import java.util.*;

/**
 * Controller for REST-Api.
 *
 * @author Lukas Taake
 */
@RestController
@RequestMapping("/rest")
public class RestApiController {

    private static final Logger LOGGER = LoggerFactory.getLogger(RestApiController.class);

    private final XmlDocumentReader xmlReader;
    private final XmlDocumentWriter xmlWriter;
    private final RawDocumentProcessor documentProcessor;
    private final DocumentDao documentDao;
    private final AccessDao accessDao;
    private final ExternalModuleDao moduleDao;
    private final ClassificationDao classificationDao;

    @Autowired
    public RestApiController(XmlDocumentReader xmlReader, XmlDocumentWriter xmlWriter, RawDocumentProcessor documentProcessor,
                             DocumentDao documentDao, AccessDao accessDao, ExternalModuleDao moduleDao, ClassificationDao classificationDao) {
        this.xmlReader = xmlReader;
        this.xmlWriter = xmlWriter;
        this.documentProcessor = documentProcessor;
        this.documentDao = documentDao;
        this.accessDao = accessDao;
        this.moduleDao = moduleDao;
        this.classificationDao = classificationDao;
    }

    /**
     * Print out a list of all documents, containing only their metadata.
     * Intended for: any kind of user interface (like a single-page-app, or a mobile app).
     */
    @RequestMapping(path = "/document", method = RequestMethod.GET, produces = MediaType.APPLICATION_XML_VALUE)
    public String listDocuments(HttpServletResponse response) {
        List<DocumentStub> documents = documentDao.findAllStubs();
        try {
            return xmlWriter.writeStubList(documents);
        } catch (XMLStreamException e) {
            return handleServerError(response, e);
        }
    }

    /**
     * Put one or more new documents into the newsboard.
     * Intended for: external crawler modules
     */
    @RequestMapping(path = "/document", method = RequestMethod.PUT)
    public String putDocument(HttpServletRequest request, HttpServletResponse response, @RequestBody String body) {
        Optional<ExternalModule> authenticationResult = checkAuthentication(request);
        if (!authenticationResult.isPresent()) {
            return handleAuthenticationError(response);
        }
        ExternalModule crawler = authenticationResult.get();
        try {
            xmlReader.readDocument(new StringReader(body), (title, author, source, creationTime, crawlTime, rawText) -> {
                RawDocument rawDocument = new RawDocument(title, author, source, creationTime, crawlTime, crawler.getId(), rawText);
                Document document = documentProcessor.processDocument(rawDocument);
                documentDao.create(document);
            });
        } catch (XmlException e) {
            return handleClientError(response, e);
        }
        return "OK";
    }

    /**
     * Print out a single document in it's complete structure including sentences and their classifications.
     * Intended for: external UI modules
     */
    @RequestMapping(value = "/document/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_XML_VALUE)
    public String getDocument(HttpServletResponse response, @PathVariable String id) {
        int docId = Integer.parseInt(id);
        Document doc = documentDao.get(docId);
        List<DocumentClassification> classifications = classificationDao.forForDocument(doc);
        try {
            return xmlWriter.writeDocument(doc, classifications);
        } catch (XMLStreamException e) {
            return handleServerError(response, e);
        }
    }

    /**
     * Print out a list of all documents that aren't classified by a specified external module.
     * Intended for: external classifiers modules
     */
    @RequestMapping(path = "/unclassified/{moduleId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_XML_VALUE)
    public String getUnclassified(HttpServletResponse response, @PathVariable String moduleId) {
        List<Document> documents = documentDao.findUnclassifiedForModule(new ModuleId(moduleId));
        Map<Document, List<DocumentClassification>> classificationMap = new HashMap<>();
        for (Document document : documents) {
            classificationMap.put(document, classificationDao.forForDocument(document));
        }
        try {
            return xmlWriter.writeDocumentList(classificationMap);
        } catch (XMLStreamException e) {
            return handleServerError(response, e);
        }
    }

    /**
     * Put one or more new classifications for existing documents into the newsboard.
     * Intended for: external classifier modules
     */
    @RequestMapping(path = "/classify", method = RequestMethod.PUT)
    public String classify(HttpServletRequest request, HttpServletResponse response, @RequestBody String body) {
        Optional<ExternalModule> authenticationResult = checkAuthentication(request);
        if (!authenticationResult.isPresent()) {
            return handleAuthenticationError(response);
        }
        ExternalModule classifier = authenticationResult.get();
        List<ClassificationValue> values = new ArrayList<>();
        StringReader in = new StringReader(body);
        try {
            xmlReader.readClassifications(in,
                    new ClassificationParsedHandler() {
                        @Override
                        public void onValueParsed(double value, OptionalDouble confidence) {
                            if (confidence.isPresent()) {
                                values.add(ClassificationValue.of(value, confidence.getAsDouble()));
                            } else {
                                values.add(ClassificationValue.of(value));
                            }
                        }

                        @Override
                        public void onClassificationParsed(int documentId, String classifierId) {
                            if (!classifierId.equals(classifier.getId().raw())) {
                                handleClientError(response, new Exception("Authentication doesn't match supplied classifier name"));
                            }
                            Document document = documentDao.get(documentId);
                            DocumentClassification classification = document.addClassification(new ModuleId(classifierId), values);
                            classificationDao.create(classification);
                        }
                    });
        } catch (Exception e) {
            return handleClientError(response, e);
        }
        LOGGER.info("Added new classification");
        return "OK";
    }

    private Optional<ExternalModule> checkAuthentication(HttpServletRequest request) {
        String authorization = request.getHeader("Authorization");
        if (authorization != null && authorization.startsWith("Basic")) {
            String base64Credentials = authorization.substring("Basic".length()).trim();
            String credentials = new String(Base64.getDecoder().decode(base64Credentials), Charset.forName("UTF-8"));
            String[] values = credentials.split(":", 2);
            ExternalModule module = moduleDao.get(new ModuleId(values[0]));
            if (module != null) {
                Access access = accessDao.get(module.getAccessId());
                if (access.isEnabled() && access.getPassphrase().equals(values[1])) {
                    return Optional.of(module);
                }
            }
        }
        return Optional.empty();
    }

    private String handleClientError(HttpServletResponse response, Exception e) {
        LOGGER.warn("Client error: {}", e.getMessage());
        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        return "FAILED: " + e.getMessage();
    }

    private String handleServerError(HttpServletResponse response, XMLStreamException e) {
        LOGGER.warn("Server error occurred", e);
        response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        return "Internal Error: " + e.getMessage();
    }

    private String handleAuthenticationError(HttpServletResponse response) {
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        return "Authentication failed.";
    }
}
