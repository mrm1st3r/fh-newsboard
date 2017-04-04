package de.fh_bielefeld.newsboard.dao;

import de.fh_bielefeld.newsboard.model.ExternalModule;
import de.fh_bielefeld.newsboard.model.ModuleReference;

/**
 * Data access interface for external modules.
 */
public interface ExternalModuleDao {

    ExternalModule get(ModuleReference reference);

    int update(ExternalModule externalModule);

    int create(ExternalModule externalModule);
}
