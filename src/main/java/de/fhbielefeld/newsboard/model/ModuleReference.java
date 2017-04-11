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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ModuleReference that = (ModuleReference) o;

        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
