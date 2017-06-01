package de.fhbielefeld.newsboard.model.module;

import de.fhbielefeld.newsboard.model.Repository;

/**
 * Data access interface for external modules.
 */
public interface ExternalModuleDao extends Repository {

    ExternalModule get(ModuleId reference);

    int update(ExternalModule externalModule);

    int create(ExternalModule externalModule);
}
