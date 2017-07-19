package de.fh_bielefeld.newsboard.model;

import de.smartsquare.ddd.annotations.DDDValueObject;

/**
 * Model class that represents a raw document as received from a crawler.
 */
@DDDValueObject
public class RawDocument {

    private DocumentMetaData metaData;
    private String rawText;

    public DocumentMetaData getMetaData() {
        return metaData;
    }

    public void setMetaData(DocumentMetaData metaData) {
        this.metaData = metaData;
    }

    public String getRawText() {
        return rawText;
    }

    public void setRawText(String rawText) {
        this.rawText = rawText;
    }
}
