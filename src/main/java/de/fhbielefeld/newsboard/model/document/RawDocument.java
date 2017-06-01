package de.fhbielefeld.newsboard.model.document;

import de.fhbielefeld.newsboard.model.ValueObject;
import de.fhbielefeld.newsboard.model.module.ModuleId;

import java.util.Calendar;

/**
 * Model class that represents a raw document as received from a crawler.
 */
public class RawDocument implements ValueObject {

    private final DocumentMetaData metaData;
    private final String rawText;

    public RawDocument(String title, String author, String source, Calendar creationTime, Calendar crawlTime,
                       ModuleId crawler, String rawText) {
        this.metaData = new DocumentMetaData(title, author, source, creationTime, crawlTime, crawler);
        this.rawText = rawText;
    }

    public DocumentMetaData getMetaData() {
        return metaData;
    }

    public String getRawText() {
        return rawText;
    }
}
