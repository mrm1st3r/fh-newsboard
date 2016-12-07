package de.fh_bielefeld.newsboard.model;

/**
 * Model class for all kinds of external modules, like crawlers or classifiers.
 */
public abstract class ExternalModule {

    private String id;
    private String name;
    private String author;
    private String description;

    public ExternalModule(String id, String name, String author, String description) {
        this.id = id;
        this.name = name;
        this.author = author;
        this.description = description;
    }

    public String getId() {
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
}
