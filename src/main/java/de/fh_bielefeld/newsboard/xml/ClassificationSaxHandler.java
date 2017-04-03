package de.fh_bielefeld.newsboard.xml;

import de.fh_bielefeld.newsboard.model.Classification;
import de.fh_bielefeld.newsboard.model.ExternalModule;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.ArrayList;
import java.util.OptionalDouble;

/**
 * SAX handler for reading documents with classifications received on PUT /document.
 */
class ClassificationSaxHandler extends DefaultHandler {
    private ArrayList<Classification> classifications;
    private boolean insideClassification;

    private int sentenceId;
    private OptionalDouble confidence;
    private String classifierId;
    private double value;

    ClassificationSaxHandler(ArrayList<Classification> classifications) {
        this.classifications = classifications;
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        if (qName.equals("classification")) {
            parseAttributes(attributes);
            insideClassification = true;
        }
    }

    private void parseAttributes(Attributes attr) {
        sentenceId = Integer.parseInt(attr.getValue("sentenceid"));
        classifierId = attr.getValue("classifier");
        if (attr.getIndex("confidence") >= 0) {
            confidence = OptionalDouble.of(Double.parseDouble(attr.getValue("confidence")));
        } else {
            confidence = OptionalDouble.empty();
        }
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        if (insideClassification) {
            value = Double.parseDouble(new String(ch, start, length).trim());
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        if (qName.equals("classification")) {
            classifications.add(new Classification(sentenceId, new ExternalModule(classifierId), value, confidence));
        }
    }

    @Override
    public void error(SAXParseException e) throws SAXException {
        if (e != null) {
            throw e;
        }
    }
}
