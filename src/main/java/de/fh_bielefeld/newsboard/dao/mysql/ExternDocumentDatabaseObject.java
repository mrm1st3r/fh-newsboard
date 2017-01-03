package de.fh_bielefeld.newsboard.dao.mysql;

/**
 * Created by felixmeyer on 03.01.17.
 */
class ExternDocumentDatabaseObject {
    private Integer id;
    private String title;
    private String html;
    private String moduleId;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
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

    public String getModuleId() {
        return moduleId;
    }

    public void setModuleId(String moduleId) {
        this.moduleId = moduleId;
    }
}
