package de.fhbielefeld.newsboard.model;

import de.smartsquare.ddd.annotations.DDDEntity;

/**
 * Domain class representing external modules like crawler and classifiers, which are not directly part of the newsboard.
 *
 * @author Felix Meyer
 */
@DDDEntity
public class ExternalModule extends ModuleReference {
    private String name;
    private String author;
    private String description;
    private AccessReference access;

    public ExternalModule(String id, String name, String author, String description, AccessReference access) {
        super(id);
        this.name = name;
        this.author = author;
        this.description = description;
        this.access = access;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof ExternalModule)) {
            return false;
        }
        ExternalModule that = (ExternalModule) obj;
        return this.getId().equals(that.getId());
    }

    @Override
    public int hashCode() {
        return getId().hashCode() + getAuthor().hashCode();
    }

    public AccessReference getAccessReference() {
        return access;
    }
}
