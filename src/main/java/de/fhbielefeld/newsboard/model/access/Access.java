package de.fhbielefeld.newsboard.model.access;

import de.fhbielefeld.newsboard.model.Aggregate;

/**
 * An access contains login credentials and an access role
 * for authenticating and authorizing a user.
 */
public class Access extends AccessReference implements Aggregate<Access> {

    private final AccessRole role;
    private String passphrase;
    private String hashType;
    private boolean enabled;

    public Access(String id, AccessRole role, String passphrase, String hashType, boolean enabled) {
        super(id);
        this.role = role;
        this.passphrase = passphrase;
        this.hashType = hashType;
        this.enabled = enabled;
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
        return this.getId().equals(that.getId())
                && this.passphrase.equals(that.passphrase)
                && this.enabled == that.enabled;
    }

    @Override
    public int hashCode() {
        return getId().hashCode() + getPassphrase().hashCode() + getHashType().hashCode();
    }
}
