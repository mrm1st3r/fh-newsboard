package de.fhbielefeld.newsboard.model;

import de.fhbielefeld.newsboard.model.document.DocumentStub;
import de.fhbielefeld.newsboard.model.module.ModuleReference;

import java.util.Calendar;

/**
 * Model class that represents a raw document as received from a crawler.
 */
public class RawDocument extends DocumentStub {

    private final String rawText;

    public RawDocument(String title, String author, String source, Calendar creationTime, Calendar crawlTime,
                       ModuleReference crawler, String rawText) {
        super(-1, title, author, source, creationTime, crawlTime, crawler);
        this.rawText = rawText;
    }

    public String getRawText() {
        return rawText;
    }
}
