package de.fhbielefeld.newsboard.xml;

import de.fhbielefeld.newsboard.model.document.*;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import java.util.List;
import java.util.Map;

/**
 * Write complete document-structure to xml.
 * Acts as a decorator for DocumentStubWriter to write meta data.
 */
class DocumentWriter implements XmlDocumentWriter.DocumentContentWriter<Map.Entry<Document, List<DocumentClassification>>> {

    private final DocumentStubWriter stubWriter = new DecoratedStubWriter();
    private final Map<Document, List<DocumentClassification>> mapping;

    DocumentWriter(Map<Document, List<DocumentClassification>> map) {
        this.mapping = map;
    }

    @Override
    public void writeContent(XMLStreamWriter writer, Map.Entry<Document, List<DocumentClassification>> doc)
            throws XMLStreamException {
        stubWriter.writeContent(writer, doc.getKey());
    }

    private class DecoratedStubWriter extends DocumentStubWriter {
        @Override
        void writeSubElements(XMLStreamWriter writer, DocumentStub doc) throws XMLStreamException {
            writeSentences(writer, (Document) doc);
            writeClassifications(writer, (Document) doc);
        }

        private void writeSentences(XMLStreamWriter writer, Document doc) throws XMLStreamException {
            writer.writeStartElement("sentences");
            for (Sentence sent : doc.getSentences()) {
                writer.writeStartElement("sentence");
                writer.writeAttribute("id", Integer.toString(sent.getId()));
                writer.writeCharacters(sent.getText());
                writer.writeEndElement();
            }
            writer.writeEndElement();
        }

        private void writeClassifications(XMLStreamWriter writer, Document doc) throws XMLStreamException {
            writer.writeStartElement("classifications");
            for (DocumentClassification c : mapping.get(doc)) {
                int sentenceNumber = 1;
                for (ClassificationValue v : c.getValues()) {
                    writer.writeStartElement("classification");
                    writer.writeAttribute("sentence", Integer.toString(sentenceNumber));
                    writer.writeAttribute("confidence", Double.toString(v.getConfidence()));
                    writer.writeCharacters(Double.toString(v.getValue()));
                    writer.writeEndElement();
                    sentenceNumber++;
                }
            }
            writer.writeEndElement();
        }
    }
}
