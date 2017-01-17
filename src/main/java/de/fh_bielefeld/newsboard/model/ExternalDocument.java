package de.fh_bielefeld.newsboard.model;

/**
 * Domain class representing external documents, which may not be classified.
 *
 * @Author Felix Meyer
 */
public class ExternalDocument {
    private int id;
    private String title;
    private String html;
    private ExternalModule externalModule;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getHtml() {
        return html;
    }

    public void setHtml(String html) {
        this.html = html;
    }

    public ExternalModule getExternalModule() {
        return externalModule;
    }

    public void setExternalModule(ExternalModule externalModule) {
        this.externalModule = externalModule;
    }
}
