package de.fh_bielefeld.newsboard.model;

import java.util.Calendar;

/**
 * This class represents a stub document, that contains only meta data but no sentences or classifications.
 */
public class DocumentStub {
    private int id;
    private final String title;
    private final String author;
    private final String source;
    private final Calendar creationTime;
    private final Calendar crawlTime;
    private final ModuleReference crawler;

    public DocumentStub(int id, String title, String author, String source, Calendar creationTime, Calendar crawlTime,
                        ModuleReference crawler) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.source = source;
        this.creationTime = creationTime;
        this.crawlTime = crawlTime;
        this.crawler = crawler;
    }

    DocumentStub(DocumentStub stub) {
        this.id = stub.id;
        this.title = stub.title;
        this.author = stub.author;
        this.source = stub.source;
        this.creationTime = stub.creationTime;
        this.crawlTime = stub.crawlTime;
        this.crawler = stub.crawler;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        if (this.id != -1) {
            throw new IllegalStateException("Document has already an ID assigned");
        }
        this.id = id;
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

    public ModuleReference getModule() {
        return crawler;
    }
}
