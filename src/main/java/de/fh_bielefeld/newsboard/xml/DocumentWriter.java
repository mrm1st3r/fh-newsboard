package de.fh_bielefeld.newsboard.xml;

import de.fh_bielefeld.newsboard.model.Classification;
import de.fh_bielefeld.newsboard.model.Document;
import de.fh_bielefeld.newsboard.model.DocumentStub;
import de.fh_bielefeld.newsboard.model.Sentence;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import java.util.ArrayList;
import java.util.List;

/**
 * Write complete document-structure to xml.
 * Acts as a decorator for DocumentStubWriter to write meta data.
 */
class DocumentWriter implements XmlDocumentWriter.DocumentContentWriter<Document> {

    private List<Classification> collectedClassifications;

    @Override
    public void writeContent(XMLStreamWriter writer, Document doc) throws XMLStreamException {
        stubWriter.writeContent(writer, doc);
    }

    private final DocumentStubWriter stubWriter = new DocumentStubWriter() {
        @Override
        void writeSubElements(XMLStreamWriter writer, DocumentStub doc) throws XMLStreamException {
            collectedClassifications = new ArrayList<>();
            writeSentences(writer, (Document) doc);
            writeClassifications(writer);
        }

        private void writeSentences(XMLStreamWriter writer, Document doc) throws XMLStreamException {
            writer.writeStartElement("sentences");
            for (Sentence sent : doc.getSentences()) {
                collectedClassifications.addAll(sent.getClassifications());
                writer.writeStartElement("sentence");
                writer.writeAttribute("id", Integer.toString(sent.getId()));
                writer.writeCharacters(sent.getText());
                writer.writeEndElement();
            }
            writer.writeEndElement();
        }

        private void writeClassifications(XMLStreamWriter writer) throws XMLStreamException {
            writer.writeStartElement("classifications");
            for (Classification c : collectedClassifications) {
                writer.writeStartElement("classification");
                writer.writeAttribute("sentenceid", Integer.toString(c.getSentenceId()));
                if (c.getConfidence().isPresent()) {
                    writer.writeAttribute("confidence", Double.toString(c.getConfidence().getAsDouble()));
                }
                writer.writeCharacters(Double.toString(c.getValue()));
                writer.writeEndElement();
            }
            writer.writeEndElement();
        }
    };
}
