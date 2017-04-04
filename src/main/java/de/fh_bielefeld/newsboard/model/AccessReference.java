package de.fh_bielefeld.newsboard.model;

/**
 * Reference to an access to be used within other aggregates.
 */
public class AccessReference {

    private final String id;

    public AccessReference(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }
}
