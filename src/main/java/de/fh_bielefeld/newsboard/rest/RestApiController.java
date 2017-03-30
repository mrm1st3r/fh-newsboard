package de.fh_bielefeld.newsboard.rest;

import de.fh_bielefeld.newsboard.dao.*;
import de.fh_bielefeld.newsboard.model.*;
import de.fh_bielefeld.newsboard.processing.RawDocumentProcessor;
import de.fh_bielefeld.newsboard.xml.XmlDocumentReader;
import de.fh_bielefeld.newsboard.xml.XmlDocumentWriter;
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
    private final ClassificationDao classificationDao;
    private final AuthenticationTokenDao tokenDao;
    private final ExternalModuleDao moduleDao;

    @Autowired
    public RestApiController(XmlDocumentReader xmlReader, XmlDocumentWriter xmlWriter, RawDocumentProcessor documentProcessor,
                             DocumentDao documentDao, ClassificationDao classificationDao, AuthenticationTokenDao tokenDao,
                             ExternalModuleDao moduleDao) {
        this.xmlReader = xmlReader;
        this.xmlWriter = xmlWriter;
        this.documentProcessor = documentProcessor;
        this.documentDao = documentDao;
        this.classificationDao = classificationDao;
        this.tokenDao = tokenDao;
        this.moduleDao = moduleDao;
    }

    /**
     * Print out a list of all documents, containing only their metadata.
     * Intended for: any kind of user interface (like a single-page-app, or a mobile app).
     */
    @RequestMapping(path = "/document", method = RequestMethod.GET, produces = MediaType.APPLICATION_XML_VALUE)
    public String listDocuments(HttpServletResponse response) {
        List<Document> documents = documentDao.findAllStubs();
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
        ExternalModule crawler;
        try {
            crawler = checkAuthentication(request);
        } catch (SecurityException e) {
            return handleAuthenticationError(response, e);
        }
        List<RawDocument> documents;
        try {
            documents = xmlReader.readDocument(new StringReader(body));
        } catch (ParserConfigurationException | IOException | SAXException e) {
            return handleClientError(response, e);
        }
        for (RawDocument rawDoc : documents) {
            rawDoc.getMetaData().setModule(crawler);
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
        int docId = Integer.valueOf(id);
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
        List<Document> documents = documentDao.findUnclassifiedForModule(moduleId);
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
        ExternalModule classifier;
        try {
            classifier = checkAuthentication(request);
        } catch (SecurityException e) {
            return handleAuthenticationError(response, e);
        }
        StringReader in = new StringReader(body);
        List<Classification> classifications;
        try {
            classifications = xmlReader.readClassifications(in);
        } catch (ParserConfigurationException | IOException | SAXException e) {
            return handleClientError(response, e);
        }
        for (Classification c : classifications) {
            if (!c.getExternalModule().equals(classifier)) {
                handleClientError(response, new Exception("Authentication doesn't match supplied classifier name"));
            }
            classificationDao.create(c);
        }
        return "OK";
    }

    private ExternalModule checkAuthentication(HttpServletRequest request) throws SecurityException {
        String authorization = request.getHeader("Authorization");
        if (authorization != null && authorization.startsWith("Basic")) {
            String base64Credentials = authorization.substring("Basic".length()).trim();
            String credentials = new String(Base64.getDecoder().decode(base64Credentials), Charset.forName("UTF-8"));
            String[] values = credentials.split(":", 2);
            Optional<AuthenticationToken> token = tokenDao.find(values[1]);
            if (token.isPresent() && token.get().getModuleId().equals(values[0])) {
                return moduleDao.get(values[0]);
            }
        }
        throw new SecurityException("Authentication failed!");
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

    private String handleAuthenticationError(HttpServletResponse response, SecurityException e) {
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        return "Authentication Error: " + e.getMessage();
    }
}
