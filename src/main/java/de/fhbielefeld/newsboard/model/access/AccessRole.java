package de.fhbielefeld.newsboard.model.access;

import de.fhbielefeld.newsboard.model.ValueObject;
import lombok.Data;

/**
 * An Access Role grants access to specific parts of the application.
 */
@Data
public class AccessRole implements ValueObject {

    private final String role;
}
