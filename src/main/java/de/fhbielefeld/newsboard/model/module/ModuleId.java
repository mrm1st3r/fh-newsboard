package de.fhbielefeld.newsboard.model.module;

import de.fhbielefeld.newsboard.model.ValueObject;

/**
 * Reference to an external module to be used within other aggregates.
 */
public class ModuleId implements ValueObject {

    private final String id;

    public ModuleId(String id) {
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
        if (!(o instanceof ModuleId)) {
            return false;
        }

        ModuleId that = (ModuleId) o;

        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public String toString() {
        return "ModuleId{" +
                "id='" + id + '\'' +
                '}';
    }
}
