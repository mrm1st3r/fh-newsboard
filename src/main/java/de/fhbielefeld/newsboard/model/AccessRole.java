package de.fhbielefeld.newsboard.model;

import de.smartsquare.ddd.annotations.DDDEntity;

/**
 * An Access Role grants access to specific parts of the application.
 */
@DDDEntity
public class AccessRole {

    private final String role;

    public AccessRole(String role) {
        this.role = role;
    }

    public String getRole() {
        return role;
    }
}
