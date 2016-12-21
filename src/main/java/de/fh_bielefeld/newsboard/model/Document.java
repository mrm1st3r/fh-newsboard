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
    private String title;
    private String author;
    private String source;
    private Calendar creationTime;
    private Calendar crawlTime;
    private ExternModule module;
    private List<Sentence> sentences;
    private List<Classification> classifications;

    public Document(int id, String title, String author,
                    String source, Calendar creationTime,
                    Calendar crawlTime, ExternModule module,
                    List<Sentence> sentences, List<Classification> classifications) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.source = source;
        this.creationTime = creationTime;
        this.crawlTime = crawlTime;
        this.module = module;
        this.sentences = sentences;
        this.classifications = classifications;
    }

    /**
     * Default constructor needed for SAX-Parsing.
     */
    public Document() {
        sentences = new ArrayList<>();
        classifications = new ArrayList<>();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public Calendar getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(Calendar creationTime) {
        this.creationTime = creationTime;
    }

    public Calendar getCrawlTime() {
        return crawlTime;
    }

    public void setCrawlTime(Calendar crawlTime) {
        this.crawlTime = crawlTime;
    }

    public ExternModule getModule() {
        return module;
    }

    public void setModule(ExternModule module) {
        this.module = module;
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

    public void addClassification(Classification classification) {
        classifications.add(classification);
    }

    public List<Classification> getClassifications() {
        return classifications;
    }
}
