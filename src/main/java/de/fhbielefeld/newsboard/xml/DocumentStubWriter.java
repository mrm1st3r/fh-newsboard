package de.fhbielefeld.newsboard.xml;

import de.fhbielefeld.newsboard.model.document.Document;
import de.fhbielefeld.newsboard.model.document.DocumentMetaData;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import static javax.xml.bind.DatatypeConverter.printDateTime;

/**
 * Write document-stubs to XML
 */
class DocumentStubWriter implements XmlDocumentWriter.DocumentContentWriter<Document> {

    @Override
    public void writeContent(XMLStreamWriter writer, Document doc) throws XMLStreamException {
        writer.writeStartElement("document");
        writer.writeAttribute("id", Integer.toString(doc.getId().raw()));
        DocumentMetaData meta = doc.getMetaData();
        writer.writeStartElement("meta");
        writer.writeStartElement("title");
        writer.writeCharacters(meta.getTitle());
        writer.writeEndElement();
        writer.writeStartElement("author");
        writer.writeCharacters(meta.getAuthor());
        writer.writeEndElement();
        writer.writeStartElement("source");
        writer.writeCharacters(meta.getSource());
        writer.writeEndElement();
        writer.writeStartElement("creationTime");
        writer.writeCharacters(printDateTime(meta.getCreationTime()));
        writer.writeEndElement();
        writer.writeStartElement("crawlTime");
        writer.writeCharacters(printDateTime(meta.getCrawlTime()));
        writer.writeEndElement();
        writer.writeEndElement();
        writeSubElements(writer, doc);
        writer.writeEndElement();
    }

    void writeSubElements(XMLStreamWriter writer, Document doc) throws XMLStreamException {
        // Placeholder to be overridden in {@link DocumentWriter}
    }
}
