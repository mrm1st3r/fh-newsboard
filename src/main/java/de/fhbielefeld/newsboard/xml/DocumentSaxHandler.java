package de.fhbielefeld.newsboard.xml;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.Calendar;

import static javax.xml.bind.DatatypeConverter.parseDateTime;

/**
 * Handler class for efficient XML-SAX-Parsing.
 */
class DocumentSaxHandler extends DefaultHandler {

    private final DocumentParsedHandler handler;

    private String rawText;
    private String title;
    private String author;
    private String source;
    private Calendar crawlTime;
    private Calendar creationTime;

    private enum State {
        UNDEFINED, DOCUMENT, TITLE, AUTHOR, SOURCE, CREATION_TIME, CRAWL_TIME, RAW_TEXT
    }

    private State currentState = State.UNDEFINED;

    DocumentSaxHandler(DocumentParsedHandler documentParsedHandler) {
        this.handler = documentParsedHandler;
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        switch (qName) {
            case "document":
                currentState = State.DOCUMENT;
                break;
            case "title":
                currentState = State.TITLE;
                break;
            case "author":
                currentState = State.AUTHOR;
                break;
            case "source":
                currentState = State.SOURCE;
                break;
            case "creationTime":
                currentState = State.CREATION_TIME;
                break;
            case "crawlTime":
                currentState = State.CRAWL_TIME;
                break;
            case "rawtext":
                currentState = State.RAW_TEXT;
                break;
            default:
                currentState = State.UNDEFINED;
        }
    }

    @Override
    public void error(SAXParseException e) throws SAXException {
        if (e != null) {
            throw e;
        }
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        String text = new String(ch, start, length).trim();
        switch (currentState) {
            case TITLE:
                title = text;
                break;
            case AUTHOR:
                author = text;
                break;
            case SOURCE:
                source = text;
                break;
            case CRAWL_TIME:
                crawlTime = parseDateTime(text);
                break;
            case CREATION_TIME:
                creationTime = parseDateTime(text);
                break;
            case RAW_TEXT:
                rawText = text;
                break;
            case DOCUMENT:
            case UNDEFINED:
            default:
                break;
        }
        currentState = State.UNDEFINED;
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        if ("document".equals(qName)) {
            handler.onDocumentParsed(title, author, source, creationTime, crawlTime, rawText);
        }
    }
}
