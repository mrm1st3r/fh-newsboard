package de.fhbielefeld.newsboard.model.access;

import de.fhbielefeld.newsboard.model.ValueObject;

/**
 * Reference to an access to be used within other aggregates.
 */
public class AccessId implements ValueObject {

    private final String id;

    public AccessId(String id) {
        this.id = id;
    }

    public String raw() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AccessId)) {
            return false;
        }

        AccessId accessId = (AccessId) o;

        return id != null ? id.equals(accessId.id) : accessId.id == null;
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}
