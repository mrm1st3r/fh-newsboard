package de.fh_bielefeld.newsboard.model;

/**
 * An access contains login credentials and an access role
 * for authenticating and authorizing a user.
 */
public class Access {

    private final String id;
    private final AccessRole role;
    private String passphrase;
    private String hashType;
    private boolean enabled;

    public Access(String id, AccessRole role, String passphrase, String hashType, boolean enabled) {
        this.id = id;
        this.role = role;
        this.passphrase = passphrase;
        this.hashType = hashType;
        this.enabled = enabled;
    }

    public String getId() {
        return id;
    }

    public String getPassphrase() {
        return passphrase;
    }

    public void setPassphrase(String passphrase) {
        this.passphrase = passphrase;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getHashType() {
        return hashType;
    }

    public void setHashType(String hashType) {
        this.hashType = hashType;
    }

    public AccessRole getRole() {
        return role;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof Access)) {
            return false;
        }
        Access that = (Access) obj;
        return this.id.equals(that.id)
                && this.passphrase.equals(that.passphrase)
                && this.enabled == that.enabled;
    }
}
