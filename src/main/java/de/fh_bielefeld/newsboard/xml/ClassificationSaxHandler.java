package de.fh_bielefeld.newsboard.xml;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.OptionalDouble;

/**
 * SAX handler for reading documents with classifications received on PUT /document.
 */
class ClassificationSaxHandler extends DefaultHandler {
    private final ClassificationParsedHandler handler;
    private boolean insideClassification;

    private int sentenceId;
    private OptionalDouble confidence;
    private String classifierId;
    private double value;
    private int documentId;

    ClassificationSaxHandler(ClassificationParsedHandler handler) {
        this.handler = handler;
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        if ("classification".equals(qName)) {
            parseAttributes(attributes);
            insideClassification = true;
        } else if ("document".equals(qName)) {
            if (attributes.getIndex("id") < 0) {
                throw new SAXException("No document id given");
            }
            documentId = Integer.parseInt(attributes.getValue("id"));
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
            insideClassification = false;
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        if ("classification".equals(qName)) {
            handler.onClassificationParsed(documentId, sentenceId, classifierId, value, confidence);
        }
    }

    @Override
    public void error(SAXParseException e) throws SAXException {
        if (e != null) {
            throw e;
        }
    }
}
