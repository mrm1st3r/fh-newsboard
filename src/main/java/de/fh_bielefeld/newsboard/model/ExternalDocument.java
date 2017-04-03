package de.fh_bielefeld.newsboard.model;

/**
 * Domain class representing external documents, which may not be classified.
 *
 * @author Felix Meyer
 */
public class ExternalDocument {
    private int id;
    private String title;
    private String html;
    private ExternalModule externalModule;

    public ExternalDocument(int id, String title, String html, ExternalModule module) {
        this.id = id;
        this.title = title;
        this.html = html;
        externalModule = module;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        if (this.id != -1) {
            throw new IllegalStateException("External document has already an ID assigned");
        }
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
}
