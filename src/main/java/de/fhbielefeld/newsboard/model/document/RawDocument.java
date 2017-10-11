package de.fhbielefeld.newsboard.model.document;

import de.fhbielefeld.newsboard.model.ValueObject;
import lombok.Data;

/**
 * Model class that represents a raw document as received from a crawler.
 */
@Data
public class RawDocument implements ValueObject {

    private final DocumentMetaData metaData;
    private final String rawText;
}
