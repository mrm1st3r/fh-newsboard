package de.fh_bielefeld.newsboard.model;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Domain class representing a classifiable document.
 *
 * @author Felix Meyer, Lukas Taake
 */
public class Document {
    private int id;
    private DocumentMetaData metaData;
    private List<Sentence> sentences;

    public Document(int id, DocumentMetaData metaData, List<Sentence> sentences) {
        this.id = id;
        this.metaData = metaData;
        this.sentences = sentences;
    }

    /**
     * Default constructor needed for SAX-Parsing.
     */
    public Document() {
        sentences = new ArrayList<>();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return metaData.getTitle();
    }

    public String getAuthor() {
        return metaData.getAuthor();
    }

    public String getSource() {
        return metaData.getSource();
    }

    public Calendar getCreationTime() {
        return metaData.getCreationTime();
    }

    public Calendar getCrawlTime() {
        return metaData.getCrawlTime();
    }

    public ExternalModule getModule() {
        return metaData.getModule();
    }

    public List<Sentence> getSentences() {
        return sentences;
    }

    public void setSentences(List<Sentence> sentences) {
        this.sentences = sentences;
    }

    public void addSentence(Sentence sentence) {
        sentences.add(sentence);
    }

    public void setMetaData(DocumentMetaData documentMetaData) {
        this.metaData = documentMetaData;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof Document)) {
            return false;
        }
        Document that = (Document) obj;
        return this.id == that.id;
    }
}
