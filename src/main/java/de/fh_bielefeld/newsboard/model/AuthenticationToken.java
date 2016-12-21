package de.fh_bielefeld.newsboard.model;

/**
 * Domain class representing a token for module authentication.
 *
 * @Author Felix Meyer
 */
public class AuthenticationToken {
    private int id;
    private String moduleId;
    private String token;

    public AuthenticationToken(int id, String moduleId, String token) {
        this.id = id;
        this.moduleId = moduleId;
        this.token = token;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getModuleId() {
        return moduleId;
    }

    public void setModuleId(String moduleId) {
        this.moduleId = moduleId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
