package de.fh_bielefeld.newsboard.dao;

import de.fh_bielefeld.newsboard.model.ExternalModule;

/**
 * Data access interface for external modules.
 */
public interface ExternalModuleDao {

    ExternalModule get(String id);

    int update(ExternalModule externalModule);

    int create(ExternalModule externalModule);
}
