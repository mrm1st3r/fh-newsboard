package de.fhbielefeld.newsboard.model.module;

import de.fhbielefeld.newsboard.model.Aggregate;
import de.fhbielefeld.newsboard.model.access.AccessId;

/**
 * Domain class representing external modules like crawler and classifiers, which are not directly part of the newsboard.
 *
 * @author Felix Meyer
 */
public class ExternalModule implements Aggregate<ExternalModule> {
    private final ModuleId id;
    private String name;
    private String author;
    private String description;
    private AccessId accessId;

    public ExternalModule(String id, String name, String author, String description, AccessId accessId) {
        this.id = new ModuleId(id);
        this.name = name;
        this.author = author;
        this.description = description;
        this.accessId = accessId;
    }

    public ModuleId getId() {
        return id;
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
        return this.getId().raw().equals(that.getId().raw());
    }

    @Override
    public int hashCode() {
        return getId().hashCode() + getAuthor().hashCode();
    }

    public AccessId getAccessId() {
        return accessId;
    }
}
