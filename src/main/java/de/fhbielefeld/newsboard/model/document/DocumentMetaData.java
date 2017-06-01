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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        DocumentMetaData metaData = (DocumentMetaData) o;

        if (!title.equals(metaData.title)) {
            return false;
        }
        if (!author.equals(metaData.author)) {
            return false;
        }
        if (!source.equals(metaData.source)) {
            return false;
        }
        if (!creationTime.equals(metaData.creationTime)) {
            return false;
        }
        if (!crawlTime.equals(metaData.crawlTime)) {
            return false;
        }
        return crawler.equals(metaData.crawler);
    }

    @Override
    public int hashCode() {
        int result = title.hashCode();
        result = 31 * result + author.hashCode();
        result = 31 * result + source.hashCode();
        result = 31 * result + creationTime.hashCode();
        result = 31 * result + crawlTime.hashCode();
        result = 31 * result + crawler.hashCode();
        return result;
    }
}
