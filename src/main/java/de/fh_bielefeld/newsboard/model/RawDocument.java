package de.fh_bielefeld.newsboard.model;

/**
 * Model class that represents a raw document as received from a crawler.
 */
public class RawDocument {

    private final DocumentMetaData metaData;
    private final String rawText;

    public RawDocument(DocumentMetaData metaData, String rawText) {
        this.metaData = metaData;
        this.rawText = rawText;
    }

    public DocumentMetaData getMetaData() {
        return metaData;
    }

    public String getRawText() {
        return rawText;
    }
}
