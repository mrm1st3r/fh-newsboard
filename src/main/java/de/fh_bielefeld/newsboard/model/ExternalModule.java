package de.fh_bielefeld.newsboard.model;

/**
 * Domain class representing external modules like crawler and classifiers, which are not directly part of the newsboard.
 *
 * @author Felix Meyer
 */
public class ExternalModule {
    private String id;
    private String name;
    private String author;
    private String description;

    public ExternalModule() {}

    public ExternalModule(String id, String name, String author, String description) {
        this.id = id;
        this.name = name;
        this.author = author;
        this.description = description;
    }

    /**
     * Constructor needed for SAX-parsing.
     */
    public ExternalModule(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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
        return this.id.equals(that.id);
    }
}