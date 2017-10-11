package de.fhbielefeld.newsboard.model.module;

import de.fhbielefeld.newsboard.model.ValueObject;
import lombok.Data;

/**
 * Reference to an external module to be used within other aggregates.
 */
@Data
public class ModuleId implements ValueObject {

    private final String id;

    public String raw() {
        return id;
    }
}
