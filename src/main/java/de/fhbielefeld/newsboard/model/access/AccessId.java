package de.fhbielefeld.newsboard.model.access;

import de.fhbielefeld.newsboard.model.ValueObject;
import lombok.Data;

/**
 * Reference to an access to be used within other aggregates.
 */
@Data
public class AccessId implements ValueObject {

    private final String id;

    public String raw() {
        return id;
    }
}
