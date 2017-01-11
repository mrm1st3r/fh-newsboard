package de.fh_bielefeld.newsboard.xml;

import de.fh_bielefeld.newsboard.model.Document;
import org.springframework.stereotype.Service;

import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import java.io.StringWriter;
import java.util.List;

import static javax.xml.bind.DatatypeConverter.printDateTime;

/**
 * Write XML-Documents from domain classes to be transmitted via the REST-API.
 */
@Service
public class XmlDocumentWriter {

    private static final String NAMESPACE = "http://fh-bielefeld.de/newsboard";

    private XMLOutputFactory xmlOutputFactory;

    public XmlDocumentWriter() {
        xmlOutputFactory = XMLOutputFactory.newInstance();
    }

    public String writeDocumentList(List<Document> documents) throws XMLStreamException {
        StringWriter strWriter = new StringWriter();
        XMLStreamWriter writer = xmlOutputFactory.createXMLStreamWriter(strWriter);

        writer.writeStartDocument();
        writer.writeStartElement("documents");
        writer.writeDefaultNamespace(NAMESPACE);

        for (Document doc : documents) {
            writeDocument(writer, doc);
        }

        writer.writeEndElement();
        writer.writeEndDocument();

        return strWriter.getBuffer().toString();
    }

    private void writeDocument(XMLStreamWriter writer, Document doc) throws XMLStreamException {
        writer.writeStartElement("document");
        writer.writeAttribute("id", Integer.toString(doc.getId()));
        writer.writeStartElement("meta");
        writer.writeStartElement("title");
        writer.writeCharacters(doc.getTitle());
        writer.writeEndElement();
        writer.writeStartElement("author");
        writer.writeCharacters(doc.getAuthor());
        writer.writeEndElement();
        writer.writeStartElement("source");
        writer.writeCharacters(doc.getSource());
        writer.writeEndElement();
        writer.writeStartElement("creationTime");
        writer.writeCharacters(printDateTime(doc.getCreationTime()));
        writer.writeEndElement();
        writer.writeStartElement("crawlTime");
        writer.writeCharacters(printDateTime(doc.getCrawlTime()));
        writer.writeEndElement();
        writer.writeEndElement();
        writer.writeEndElement();
    }
}
