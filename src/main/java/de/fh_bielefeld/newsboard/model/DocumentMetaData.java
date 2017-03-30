package de.fh_bielefeld.newsboard.model;

import java.util.Calendar;

/**
 * Meta-data that belongs either to a raw or processed document.
 */
public class DocumentMetaData {

    private final String title;
    private final String author;
    private final String source;
    private final Calendar creationTime;
    private final Calendar crawlTime;
    private final ExternalModule module;

    public DocumentMetaData(String title, String author, String source,
                            Calendar creationTime, Calendar crawlTime, ExternalModule crawler) {
        this.title = title;
        this.author = author;
        this.source = source;
        this.creationTime = creationTime;
        this.crawlTime = crawlTime;
        module = crawler;
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

    public ExternalModule getModule() {
        return module;
    }
}
