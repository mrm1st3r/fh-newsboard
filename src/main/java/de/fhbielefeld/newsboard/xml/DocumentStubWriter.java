package de.fhbielefeld.newsboard.xml;

import de.fhbielefeld.newsboard.model.DocumentStub;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import static javax.xml.bind.DatatypeConverter.printDateTime;

/**
 * Write document-stubs to XML
 */
class DocumentStubWriter implements XmlDocumentWriter.DocumentContentWriter<DocumentStub> {

    @Override
    public void writeContent(XMLStreamWriter writer, DocumentStub doc) throws XMLStreamException {
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
        writeSubElements(writer, doc);
        writer.writeEndElement();
    }

    void writeSubElements(XMLStreamWriter writer, DocumentStub doc) throws XMLStreamException {
        // Placeholder to be overridden in {@link DocumentWriter}
    }
}
