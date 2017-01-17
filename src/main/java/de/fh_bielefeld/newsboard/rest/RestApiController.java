package de.fh_bielefeld.newsboard.rest;

import de.fh_bielefeld.newsboard.dao.ClassificationDao;
import de.fh_bielefeld.newsboard.dao.DocumentDao;
import de.fh_bielefeld.newsboard.model.Classification;
import de.fh_bielefeld.newsboard.model.Document;
import de.fh_bielefeld.newsboard.model.RawDocument;
import de.fh_bielefeld.newsboard.xml.XmlDocumentReader;
import de.fh_bielefeld.newsboard.xml.XmlDocumentWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.xml.sax.SAXException;

import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.stream.XMLStreamException;
import java.io.IOException;
import java.io.StringReader;
import java.util.List;

/**
 * Controller for REST-Api.
 *
 * @author Lukas Taake
 */
@RestController
@RequestMapping("/rest")
public class RestApiController {

    private XmlDocumentReader xmlReader;
    private XmlDocumentWriter xmlWriter;
    private DocumentDao documentDao;
    private ClassificationDao classificationDao;

    @Autowired
    public RestApiController(XmlDocumentReader xmlReader, XmlDocumentWriter xmlWriter, DocumentDao documentDao, ClassificationDao classificationDao) {
        this.xmlReader = xmlReader;
        this.xmlWriter = xmlWriter;
        this.documentDao = documentDao;
        this.classificationDao = classificationDao;
    }

    /**
     * Print out a list of all documents, containing only their metadata.
     * Intended for: any kind of user interface (like a single-page-app, or a mobile app).
     */
    @RequestMapping(path = "/document", method = RequestMethod.GET, produces = MediaType.APPLICATION_XML_VALUE)
    public String listDocuments(HttpServletResponse response) {
        List<Document> documents = documentDao.getAllDocumentsOnlyWithMetaData();
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
    public String putDocument(HttpServletResponse response, @RequestBody String body) {
        StringReader in = new StringReader(body);
        List<RawDocument> documents;
        try {
            documents = xmlReader.readDocument(in);
        } catch (ParserConfigurationException | IOException | SAXException e) {
            return handleClientError(response, e);
        }
        if (documents == null || documents.size() == 0) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
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
        Document doc = documentDao.getDocumentWithId(docId);
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
    @RequestMapping(path = "/unclassified/{moduleid}", method = RequestMethod.GET, produces = MediaType.APPLICATION_XML_VALUE)
    public String getUnclassified(HttpServletResponse response, @PathVariable String moduleid) {
        List<Document> documents = documentDao.getUnclassifiedDocumentStubs(moduleid);
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
    public String classify(HttpServletResponse response, @RequestBody String body) {
        StringReader in = new StringReader(body);
        List<Classification> classifications;
        try {
            classifications = xmlReader.readClassifications(in);
        } catch (ParserConfigurationException | IOException | SAXException e) {
            return handleClientError(response, e);
        }
        for (Classification c : classifications) {
            classificationDao.insertClassification(c);
        }
        return "OK";
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
}
