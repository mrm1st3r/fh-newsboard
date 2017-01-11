package de.fh_bielefeld.newsboard.xml;

import de.fh_bielefeld.newsboard.model.Classification;
import de.fh_bielefeld.newsboard.model.Document;
import de.fh_bielefeld.newsboard.model.Sentence;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import java.util.ArrayList;
import java.util.List;

/**
 * Write complete document-structure to xml.
 * Extends DocumentStubWriter and only writes nested objects.
 */
class DocumentWriter extends DocumentStubWriter {

    private List<Classification> collectedClassifications;

    @Override
    void writeSubElements(XMLStreamWriter writer, Document doc) throws XMLStreamException {
        collectedClassifications = new ArrayList<>(doc.getClassifications());
        writeSentences(writer, doc);
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
            if (c.getDocumentId().isPresent()) writer.writeAttribute("documentid", Integer.toString(c.getDocumentId().getAsInt()));
            if (c.getSentenceId().isPresent()) writer.writeAttribute("sentenceid", Integer.toString(c.getSentenceId().getAsInt()));
            if (c.getConfidence().isPresent()) writer.writeAttribute("confidence", Double.toString(c.getConfidence().getAsDouble()));
            writer.writeCharacters(Double.toString(c.getValue()));
            writer.writeEndElement();
        }
        writer.writeEndElement();
    }
}
