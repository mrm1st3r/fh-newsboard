package de.fhbielefeld.newsboard.model.access;

import de.fhbielefeld.newsboard.model.Aggregate;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * An access contains login credentials and an access role
 * for authenticating and authorizing a user.
 */
@Data
@AllArgsConstructor
public class Access implements Aggregate<Access> {

    private final AccessId id;
    private final AccessRole role;
    private String passphrase;
    private String hashType;
    private boolean isEnabled;
}
