package de.fhbielefeld.newsboard.model.access;

import de.fhbielefeld.newsboard.model.Repository;

/**
 * Data access interface for authentication tokens used by external modules.
 */
public interface AccessDao extends Repository {

    Access get(AccessId reference);

    int update(Access authToken);

    int create(Access authToken);

}
