package de.fh_bielefeld.newsboard.dao;

import de.fh_bielefeld.newsboard.model.Access;
import de.fh_bielefeld.newsboard.model.AccessReference;

/**
 * Data access interface for authentication tokens used by external modules.
 */
public interface AccessDao {

    Access get(AccessReference reference);

    int update(Access authToken);

    int create(Access authToken);

}
