package de.fhbielefeld.newsboard.xml;

import de.fhbielefeld.newsboard.model.document.Document;
import de.fhbielefeld.newsboard.model.document.DocumentClassification;
import de.fhbielefeld.newsboard.model.document.DocumentStub;
import org.springframework.stereotype.Service;

import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Write XML-Documents from domain classes to be transmitted via the REST-API.
 */
@Service
public class XmlDocumentWriter {

    private static final String NAMESPACE = "http://fh-bielefeld.de/newsboard";

    private final XMLOutputFactory xmlOutputFactory;

    public XmlDocumentWriter() {
        xmlOutputFactory = XMLOutputFactory.newInstance();
    }

    public String writeStubList(List<DocumentStub> documents) throws XMLStreamException {
        return writeDocument(new DocumentStubWriter(), documents);
    }

    public String writeDocument(Document document, List<DocumentClassification> classifications) throws XMLStreamException {
        Map<Document, List<DocumentClassification>> map = new HashMap<>();
        map.put(document, classifications);
        return writeDocument(new DocumentWriter(map), map.entrySet());
    }

    public String writeDocumentList(Map<Document, List<DocumentClassification>> documents) throws XMLStreamException {
        return writeDocument(new DocumentWriter(documents), documents.entrySet());
    }

    private <T> String writeDocument(DocumentContentWriter<T> contentWriter, Iterable<T> documents) throws XMLStreamException {
        StringWriter strWriter = new StringWriter();
        XMLStreamWriter writer = xmlOutputFactory.createXMLStreamWriter(strWriter);

        writer.writeStartDocument();
        writer.writeStartElement("documents");
        writer.writeDefaultNamespace(NAMESPACE);

        for (T doc : documents) {
            contentWriter.writeContent(writer, doc);
        }

        writer.writeEndElement();
        writer.writeEndDocument();

        return strWriter.getBuffer().toString();
    }

    interface DocumentContentWriter<T> {
        void writeContent(XMLStreamWriter writer, T doc) throws XMLStreamException;
    }
}
