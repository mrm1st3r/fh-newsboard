package de.fh_bielefeld.newsboard.model;

/**
 * Domain class representing external modules like crawler and classifiers, which are not directly part of the newsboard.
 *
 * @author Felix Meyer
 */
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

    public AccessReference getAccessReference() {
        return access;
    }
}
