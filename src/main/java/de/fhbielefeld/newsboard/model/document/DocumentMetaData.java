package de.fhbielefeld.newsboard.model.document;

import de.fhbielefeld.newsboard.model.ValueObject;
import de.fhbielefeld.newsboard.model.module.ModuleId;
import lombok.Data;

import java.util.Calendar;

/**
 * This class represents a set of metadata for a document.
 */
@Data
public class DocumentMetaData implements ValueObject {
    private final String title;
    private final String author;
    private final String source;
    private final Calendar creationTime;
    private final Calendar crawlTime;
    private final ModuleId crawler;

    public ModuleId getModule() {
        return crawler;
    }
}
