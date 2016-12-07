package de.fh_bielefeld.newsboard.model;

import java.util.Calendar;
import java.util.List;

/**
 * Model class for News-Documents.
 *
 * @author Lukas Taake
 */
public class Document {

    private int id;
    private String title;
    private String author;
    private String source;
    private Calendar creationTime;
    private Calendar crawlTime;

    private List<Sentence> sentences;
    private List<Classification> classifications;

    public Document(int id, String title, String author, String source, Calendar creationTime, Calendar crawlTime,
                    List<Sentence> sentences, List<Classification> classifications) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.source = source;
        this.creationTime = creationTime;
        this.crawlTime = crawlTime;
        this.sentences = sentences;
        this.classifications = classifications;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public String getSource() {
        return source;
    }

    public Calendar getCreationTime() {
        return creationTime;
    }

    public Calendar getCrawlTime() {
        return crawlTime;
    }

    public List<Sentence> getSentences() {
        return sentences;
    }

    public List<Classification> getClassifications() {
        return classifications;
    }

    public void addClassification(Classification classification) {
        classifications.add(classification);
    }
}
