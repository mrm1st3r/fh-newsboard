package de.fh_bielefeld.newsboard.dao;

import de.fh_bielefeld.newsboard.model.ExternalModule;
import de.smartsquare.ddd.annotations.DDDRepository;

/**
 * Data access interface for external modules.
 */
@DDDRepository
public interface ExternalModuleDao {

    ExternalModule get(String id);

    int update(ExternalModule externalModule);

    int create(ExternalModule externalModule);
}
