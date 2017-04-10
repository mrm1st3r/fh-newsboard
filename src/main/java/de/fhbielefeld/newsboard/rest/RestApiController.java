package de.fhbielefeld.newsboard.rest;

import de.fhbielefeld.newsboard.dao.AccessDao;
import de.fhbielefeld.newsboard.dao.DocumentDao;
import de.fhbielefeld.newsboard.dao.ExternalModuleDao;
import de.fhbielefeld.newsboard.model.*;
import de.fhbielefeld.newsboard.processing.RawDocumentProcessor;
import de.fhbielefeld.newsboard.xml.XmlDocumentReader;
import de.fhbielefeld.newsboard.xml.XmlDocumentWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.xml.sax.SAXException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.stream.XMLStreamException;
import java.io.IOException;
import java.io.StringReader;
import java.nio.charset.Charset;
import java.util.Base64;
import java.util.List;
import java.util.Optional;

/**
 * Controller for REST-Api.
 *
 * @author Lukas Taake
 */
@RestController
@RequestMapping("/rest")
public class RestApiController {

    private final XmlDocumentReader xmlReader;
    private final XmlDocumentWriter xmlWriter;
    private final RawDocumentProcessor documentProcessor;
    private final DocumentDao documentDao;
    private final AccessDao accessDao;
    private final ExternalModuleDao moduleDao;

    @Autowired
    public RestApiController(XmlDocumentReader xmlReader, XmlDocumentWriter xmlWriter, RawDocumentProcessor documentProcessor,
                             DocumentDao documentDao, AccessDao accessDao, ExternalModuleDao moduleDao) {
        this.xmlReader = xmlReader;
        this.xmlWriter = xmlWriter;
        this.documentProcessor = documentProcessor;
        this.documentDao = documentDao;
        this.accessDao = accessDao;
        this.moduleDao = moduleDao;
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
        List<RawDocument> documents;
        try {
            documents = xmlReader.readDocument(new StringReader(body), crawler);
        } catch (ParserConfigurationException | IOException | SAXException e) {
            return handleClientError(response, e);
        }
        for (RawDocument rawDoc : documents) {
            Document document = documentProcessor.processDocument(rawDoc);
            documentDao.create(document);
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
        try {
            return xmlWriter.writeDocument(doc);
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
        List<Document> documents = documentDao.findUnclassifiedForModule(new ModuleReference(moduleId));
        try {
            return xmlWriter.writeDocumentList(documents);
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
        StringReader in = new StringReader(body);
        try {
            xmlReader.readClassifications(in, (documentId, sentenceId, classifierId, value, confidence) -> {
                if (!classifierId.equals(classifier.getId())) {
                    handleClientError(response, new Exception("Authentication doesn't match supplied classifier name"));
                }
                Document document = documentDao.get(documentId);
                document.getSentenceById(sentenceId).addClassification(classifier, value, confidence);
                documentDao.update(document);
            });
        } catch (ParserConfigurationException | IOException | SAXException e) {
            return handleClientError(response, e);
        }
        return "OK";
    }

    private Optional<ExternalModule> checkAuthentication(HttpServletRequest request) {
        String authorization = request.getHeader("Authorization");
        if (authorization != null && authorization.startsWith("Basic")) {
            String base64Credentials = authorization.substring("Basic".length()).trim();
            String credentials = new String(Base64.getDecoder().decode(base64Credentials), Charset.forName("UTF-8"));
            String[] values = credentials.split(":", 2);
            ExternalModule module = moduleDao.get(new ModuleReference(values[0]));
            if (module != null) {
                Access access = accessDao.get(module.getAccessReference());
                if (access.isEnabled() && access.getPassphrase().equals(values[1])) {
                    return Optional.of(module);
                }
            }
        }
        return Optional.empty();
    }

    private String handleClientError(HttpServletResponse response, Exception e) {
        e.printStackTrace();
        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        return "FAILED: " + e.getMessage();
    }

    private String handleServerError(HttpServletResponse response, XMLStreamException e) {
        e.printStackTrace();
        response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        return "Internal Error: " + e.getMessage();
    }

    private String handleAuthenticationError(HttpServletResponse response) {
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        return "Authentication failed.";
    }
}
