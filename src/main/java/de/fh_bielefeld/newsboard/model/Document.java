package de.fh_bielefeld.newsboard.model;

import java.util.Calendar;

/**
 * Created by felixmeyer on 11.12.16.
 */
public class Document {
    private int id;
    private String title;
    private String author;
    private String source;
    private Calendar creationTime;
    private Calendar crawlTime;
    private String moduleId;

    public Document(int id, String title, String author, String source, Calendar creationTime, Calendar crawlTime, String moduleId) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.source = source;
        this.creationTime = creationTime;
        this.crawlTime = crawlTime;
        this.moduleId = moduleId;
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

    public String getModuleId() {
        return moduleId;
    }

    public void setModuleId(String moduleId) {
        this.moduleId = moduleId;
    }
}
