package de.fh_bielefeld.newsboard.dao;

import de.fh_bielefeld.newsboard.model.Access;

/**
 * Data access interface for authentication tokens used by external modules.
 */
public interface AccessDao {

    Access get(String id);

    int update(Access authToken);

    int create(Access authToken);

}
