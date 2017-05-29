package de.fhbielefeld.newsboard.model.access;

/**
 * Data access interface for authentication tokens used by external modules.
 */
public interface AccessDao {

    Access get(AccessReference reference);

    int update(Access authToken);

    int create(Access authToken);

}
