package de.fhbielefeld.newsboard.model.document;

import de.fhbielefeld.newsboard.model.ValueObject;
import lombok.Data;

/**
 * A classifications identity.
 */
@Data
public class ClassificationId implements ValueObject {

    private final int id;
}
