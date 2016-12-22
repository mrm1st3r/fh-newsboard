package de.fh_bielefeld.newsboard.xml;

import de.fh_bielefeld.newsboard.model.Classification;
import de.fh_bielefeld.newsboard.model.Document;
import de.fh_bielefeld.newsboard.model.ExternModule;
import de.fh_bielefeld.newsboard.model.Sentence;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.ArrayList;

/**
 * SAX handler for reading documents with classifications received on PUT /document.
 */
class ClassificationSaxHandler extends DefaultHandler {
    private ArrayList<Classification> classifications;
    private Classification classification;

    ClassificationSaxHandler(ArrayList<Classification> classifications) {
        this.classifications = classifications;
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        if (qName.equals("classification")) {
            classification = new Classification();
            parseAttributes(classification, attributes);
        }
    }

    private void parseAttributes(Classification classification, Attributes attr) {
        ExternModule classifier = new ExternModule(attr.getValue("classifier"));
        classification.setExternModule(classifier);
        if (attr.getIndex("confidence") >= 0) {
            classification.setConfidence(Double.parseDouble(attr.getValue("confidence")));
        }
        if (attr.getIndex("sentenceid") >= 0) {
            Sentence sent = new Sentence();
            sent.setId(Integer.parseInt(attr.getValue("sentenceid")));
            classification.setSentence(sent);
        }
        if (attr.getIndex("documentid") >= 0) {
            Document doc = new Document();
            doc.setId(Integer.parseInt(attr.getValue("documentid")));
            classification.setDocument(doc);
        }
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        if (classification != null) {
            double val = Double.parseDouble(new String(ch, start, length).trim());
            classification.setValue(val);
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        if (qName.equals("classification")) {
            classifications.add(classification);
            classification = null;
        }
    }
}