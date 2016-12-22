package de.fh_bielefeld.newsboard.xml;

import de.fh_bielefeld.newsboard.model.Classification;
import de.fh_bielefeld.newsboard.model.RawDocument;
import org.springframework.stereotype.Service;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.XMLConstants;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import java.io.IOException;
import java.io.Reader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Unmarshall an XML document received via the REST-API into domain classes.
 */
@Service
public class XmlDocumentReader {

    private final SAXParserFactory parserFactory;

    public XmlDocumentReader() throws SAXException {
        SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
        URL schemaFile = getClass().getResource("/document.xsd");
        Schema schema = factory.newSchema(schemaFile);

        parserFactory = SAXParserFactory.newInstance();
        parserFactory.setSchema(schema);
        parserFactory.setNamespaceAware(true);
    }

    /**
     * Read raw documents from an xml input.
     */
    public List<RawDocument> readDocument(Reader xmlDocument) throws ParserConfigurationException, SAXException, IOException {
        SAXParser parser = parserFactory.newSAXParser();
        ArrayList<RawDocument> documents = new ArrayList<>();
        DocumentSaxHandler handler = new DocumentSaxHandler(documents);
        parser.parse(new InputSource(xmlDocument), handler);
        return documents;
    }

    /**
     * Read classifications from an xml input.
     */
    public List<Classification> readClassifications(Reader xmlDocument) throws ParserConfigurationException, SAXException, IOException {
        SAXParser parser = parserFactory.newSAXParser();
        ArrayList<Classification> classifications = new ArrayList<>();
        parser.parse(new InputSource(xmlDocument), new ClassificationSaxHandler(classifications));
        return classifications;
    }
}
