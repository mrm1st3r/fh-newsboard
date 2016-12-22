package de.fh_bielefeld.newsboard.model;

import java.util.Calendar;

/**
 * Meta-data that belongs either to a raw or processed document.
 */
public class DocumentMetaData {

    private String title;
    private String author;
    private String source;
    private Calendar creationTime;
    private Calendar crawlTime;
    private ExternModule module;

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
}
