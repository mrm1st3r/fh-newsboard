package de.fh_bielefeld.newsboard.model;

import java.util.Calendar;

/**
 * This class represents a stub document, that contains only meta data but no sentences or classifications.
 */
public class DocumentStub {
    private int id;
    private DocumentMetaData metaData;

    public DocumentStub(int id, DocumentMetaData metaData) {
        this.id = id;
        this.metaData = metaData;
    }

    DocumentStub(DocumentStub stub) {
        this.id = stub.id;
        this.metaData = stub.metaData;
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
}
