package de.fhbielefeld.newsboard.model.access;

/**
 * An Access Role grants access to specific parts of the application.
 */
public class AccessRole {

    private final String role;

    public AccessRole(String role) {
        this.role = role;
    }

    public String getRole() {
        return role;
    }
}
