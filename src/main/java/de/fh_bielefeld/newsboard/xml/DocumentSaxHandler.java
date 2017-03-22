package de.fh_bielefeld.newsboard.xml;

import de.fh_bielefeld.newsboard.model.DocumentMetaData;
import de.fh_bielefeld.newsboard.model.RawDocument;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.List;

import static javax.xml.bind.DatatypeConverter.parseDateTime;

/**
 * Handler class for efficient XML-SAX-Parsing.
 */
class DocumentSaxHandler extends DefaultHandler {

    private enum State {
        UNDEFINED, DOCUMENT, TITLE, AUTHOR, SOURCE, CREATION_TIME, CRAWL_TIME, RAW_TEXT
    }

    private State currentState = State.UNDEFINED;

    private List<RawDocument> documentList;
    private RawDocument document;
    private DocumentMetaData meta;

    DocumentSaxHandler(List<RawDocument> documentList) {
        this.documentList = documentList;
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        switch (qName) {
            case "document":
                currentState = State.DOCUMENT;
                document = new RawDocument();
                meta = new DocumentMetaData();
                document.setMetaData(meta);
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
                meta.setTitle(text);
                break;
            case AUTHOR:
                meta.setAuthor(text);
                break;
            case SOURCE:
                meta.setSource(text);
                break;
            case CRAWL_TIME:
                meta.setCrawlTime(parseDateTime(text));
                break;
            case CREATION_TIME:
                meta.setCreationTime(parseDateTime(text));
                break;
            case RAW_TEXT:
                document.setRawText(text);
                break;
        }
        currentState = State.UNDEFINED;
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        switch (qName) {
            case "document":
                documentList.add(document);
                document = null;
                break;
        }
    }
}
