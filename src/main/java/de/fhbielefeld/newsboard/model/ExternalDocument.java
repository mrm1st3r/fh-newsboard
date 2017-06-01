package de.fhbielefeld.newsboard.model;

import de.fhbielefeld.newsboard.model.module.ModuleId;

/**
 * Domain class representing external documents, which may not be classified.
 *
 * @author Felix Meyer
 */
public class ExternalDocument implements Aggregate<ExternalDocument> {
    private int id;
    private String title;
    private String html;
    private ModuleId externalModule;

    public ExternalDocument(int id, String title, String html, ModuleId module) {
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

    public ModuleId getExternalModule() {
        return externalModule;
    }
}
