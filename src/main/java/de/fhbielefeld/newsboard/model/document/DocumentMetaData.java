package de.fhbielefeld.newsboard.model.document;

import de.fhbielefeld.newsboard.model.ValueObject;
import de.fhbielefeld.newsboard.model.module.ModuleId;

import java.util.Calendar;

/**
 * This class represents a set of metadata for a document.
 */
public class DocumentMetaData implements ValueObject {
    private final String title;
    private final String author;
    private final String source;
    private final Calendar creationTime;
    private final Calendar crawlTime;
    private final ModuleId crawler;

    public DocumentMetaData(String title, String author, String source, Calendar creationTime, Calendar crawlTime,
                            ModuleId crawler) {
        this.title = title;
        this.author = author;
        this.source = source;
        this.creationTime = creationTime;
        this.crawlTime = crawlTime;
        this.crawler = crawler;
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

    public ModuleId getModule() {
        return crawler;
    }
}
