package de.fhbielefeld.newsboard.dao;

import de.fhbielefeld.newsboard.model.Access;
import de.fhbielefeld.newsboard.model.AccessReference;

/**
 * Data access interface for authentication tokens used by external modules.
 */
public interface AccessDao {

    Access get(AccessReference reference);

    int update(Access authToken);

    int create(Access authToken);

}
