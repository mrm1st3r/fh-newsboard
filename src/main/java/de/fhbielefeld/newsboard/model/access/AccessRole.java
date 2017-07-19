package de.fhbielefeld.newsboard.model.access;

import de.fhbielefeld.newsboard.model.ValueObject;

/**
 * An Access Role grants access to specific parts of the application.
 */
public class AccessRole implements ValueObject {

    private final String role;

    public AccessRole(String role) {
        this.role = role;
    }

    public String getRole() {
        return role;
    }
}
