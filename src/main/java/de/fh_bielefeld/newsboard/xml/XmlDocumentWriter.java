package de.fh_bielefeld.newsboard.xml;

import de.fh_bielefeld.newsboard.model.Document;
import org.springframework.stereotype.Service;

import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import java.io.StringWriter;
import java.util.List;

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

    public String writeStubList(List<Document> documents) throws XMLStreamException {
        return writeDocument(new DocumentStubWriter(), documents);
    }

    private String writeDocument(DocumentContentWriter contentWriter, List<Document> documents) throws XMLStreamException {
        StringWriter strWriter = new StringWriter();
        XMLStreamWriter writer = xmlOutputFactory.createXMLStreamWriter(strWriter);

        writer.writeStartDocument();
        writer.writeStartElement("documents");
        writer.writeDefaultNamespace(NAMESPACE);

        for (Document doc : documents) {
            contentWriter.writeContent(writer, doc);
        }

        writer.writeEndElement();
        writer.writeEndDocument();

        return strWriter.getBuffer().toString();
    }

    interface DocumentContentWriter {
        void writeContent(XMLStreamWriter writer, Document doc) throws XMLStreamException;
    }
}
