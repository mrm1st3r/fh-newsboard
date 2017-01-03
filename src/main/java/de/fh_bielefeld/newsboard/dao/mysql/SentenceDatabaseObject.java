package de.fh_bielefeld.newsboard.dao.mysql;

/**
 * Created by felixmeyer on 03.01.17.
 */
class SentenceDatabaseObject {
    private Integer id;
    private Integer number;
    private String text;
    private Integer documentId;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Integer getDocumentId() {
        return documentId;
    }

    public void setDocumentId(Integer documentId) {
        this.documentId = documentId;
    }
}
