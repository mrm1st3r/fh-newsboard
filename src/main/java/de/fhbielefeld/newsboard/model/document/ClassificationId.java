package de.fhbielefeld.newsboard.model.document;

import de.fhbielefeld.newsboard.model.ValueObject;

/**
 * A classifications identity.
 */
public class ClassificationId implements ValueObject {

    private final int id;

    public ClassificationId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }
}
