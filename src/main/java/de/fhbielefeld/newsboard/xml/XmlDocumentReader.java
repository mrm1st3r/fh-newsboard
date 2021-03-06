package de.fhbielefeld.newsboard.xml;

import org.springframework.stereotype.Service;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.XMLConstants;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import java.io.IOException;
import java.io.Reader;
import java.net.URL;

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
    public void readDocument(Reader xmlDocument, DocumentParsedHandler documentParsedHandler) throws XmlException {
        try {
            parserFactory.newSAXParser().parse(new InputSource(xmlDocument), new DocumentSaxHandler(documentParsedHandler));
        } catch (ParserConfigurationException | SAXException | IOException e) {
            throw new XmlException(e);
        }
    }

    /**
     * Read classifications from an xml input.
     */
    public void readClassifications(Reader xmlDocument, ClassificationParsedHandler handler) throws XmlException {
        try {
            parserFactory.newSAXParser().parse(new InputSource(xmlDocument), new ClassificationSaxHandler(handler));
        } catch (ParserConfigurationException | SAXException | IOException e) {
            throw new XmlException(e);
        }
    }
}
