package de.fh_bielefeld.newsboard.rest;

import de.fh_bielefeld.newsboard.model.Document;
import de.fh_bielefeld.newsboard.xml.XmlDocumentReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.xml.sax.SAXException;

import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.ParserConfigurationException;
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

    public RestApiController(@Autowired XmlDocumentReader xmlReader) {
        this.xmlReader = xmlReader;
    }

    @RequestMapping(path = "/document", method = RequestMethod.GET)
    public String listDocuments() { return "Hello world"; }

    @RequestMapping(path = "/document", method = RequestMethod.PUT)
    public void putDocument(HttpServletResponse response, @RequestBody String body) {
        StringReader in = new StringReader(body);
        List<Document> documents = null;
        try {
            documents = xmlReader.readDocument(in);
        } catch (ParserConfigurationException | IOException | SAXException e) {
            e.printStackTrace();
        }

        if (documents == null || documents.size() == 0) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
    }

    @RequestMapping(path = "/document/{id}", method = RequestMethod.GET)
    public String getDocument() {
        return "Hello world!";
    }

    @RequestMapping(path = "/unclassified", method = RequestMethod.GET)
    public String getUnclassified() {
        return "Hello world!";
    }

    @RequestMapping(path = "/classify", method = RequestMethod.PUT)
    public String classify() {
        return "Hello world!";
    }
}
