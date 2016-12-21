package de.fh_bielefeld.newsboard.xml;

import de.fh_bielefeld.newsboard.model.Classification;
import de.fh_bielefeld.newsboard.model.Document;
import de.fh_bielefeld.newsboard.model.ExternModule;
import de.fh_bielefeld.newsboard.model.Sentence;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.List;
import java.util.Optional;

/**
 * Handler class for efficient XML-SAX-Parsing.
 */
class DocumentSaxHandler extends DefaultHandler {

    private enum State {
        UNDEFINED, DOCUMENT, TITLE, AUTHOR, SOURCE, CREATION_TIME, CRAWL_TIME, SENTENCE, CLASSIFICATION
    }

    private State currentState = State.UNDEFINED;
    private boolean containsSentences = false;

    private List<Document> documentList;
    private Document document;
    private Sentence sentence;
    private Classification classification;

    DocumentSaxHandler(List<Document> documentList) {
        this.documentList = documentList;
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        switch (qName) {
            case "document":
                currentState = State.DOCUMENT;
                document = new Document();
                if (attributes.getIndex("id") >= 0) {
                    document.setId(Integer.parseInt(attributes.getValue("id")));
                }
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
            case "sentence":
                currentState = State.SENTENCE;
                containsSentences = true;
                sentence = new Sentence();
                sentence.setId(Integer.parseInt(attributes.getValue("id")));
                break;
            case "classification":
                currentState = State.CLASSIFICATION;
                classification = new Classification();
                classification.setExternModule(new ExternModule(attributes.getValue("classifier")));
                if (attributes.getIndex("confidence") >= 0) {
                    classification.setConfidence(Double.parseDouble(attributes.getValue("confidence")));
                }
                if (attributes.getIndex("documentid") >= 0) {
                    if (Integer.parseInt(attributes.getValue("documentid")) == document.getId()) {
                        document.addClassification(classification);
                    } else {
                        throw new SAXException("Document ID doesn't match");
                    }
                }
                if (attributes.getIndex("sentenceid") >= 0) {
                    int it = Integer.parseInt(attributes.getValue("sentenceid"));
                    Optional<Sentence> s = document.getSentences().stream().filter(sent->sent.getId()==it).findFirst();
                    if (s.isPresent()) { s.get().addClassification(classification);
                    } else {
                        if (!containsSentences) {
                          Sentence sent = new Sentence();
                            sent.addClassification(classification);
                            document.addSentence(sent);
                        } else throw new SAXException("Sentence not known: " + it);
                    }
                }
                break;
        }
    }

    @Override
    public void error(SAXParseException e) throws SAXException {
        throw e;
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        String text = new String(ch, start, length).trim();
        switch (currentState) {
            case TITLE:
                document.setTitle(text);
                break;
            case AUTHOR:
                document.setAuthor(text);
                break;
            case SOURCE:
                document.setSource(text);
                break;
            case SENTENCE:
                sentence.setText(text);
                break;
            case CLASSIFICATION:
                classification.setValue(Double.parseDouble(text));
                break;
        }
        currentState = State.UNDEFINED;
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        switch (qName) {
            case "sentence":
                document.addSentence(sentence);
                sentence = null;
                break;
            case "document":
                documentList.add(document);
                document = null;
                break;
            case "classification":
                classification = null;
                break;
        }
    }
}
