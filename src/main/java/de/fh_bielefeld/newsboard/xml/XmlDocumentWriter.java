package de.fh_bielefeld.newsboard.xml;

import de.fh_bielefeld.newsboard.model.Document;
import de.fh_bielefeld.newsboard.model.DocumentStub;
import org.springframework.stereotype.Service;

import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import java.io.StringWriter;
import java.util.Collections;
import java.util.List;

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

    public String writeDocument(Document document) throws XMLStreamException {
        return writeDocument(new DocumentWriter(), Collections.singletonList(document));
    }

    public String writeDocumentList(List<Document> documents) throws XMLStreamException {
        return writeDocument(new DocumentWriter(), documents);
    }

    private <T> String writeDocument(DocumentContentWriter<T> contentWriter, List<T> documents) throws XMLStreamException {
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
