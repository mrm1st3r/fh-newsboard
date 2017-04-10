package de.fhbielefeld.newsboard.model;

/**
 * Reference to an external module to be used within other aggregates.
 */
public class ModuleReference {

    private final String id;

    public ModuleReference(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }
}
