package de.fhbielefeld.newsboard.dao;

import de.fhbielefeld.newsboard.model.ExternalModule;
import de.fhbielefeld.newsboard.model.ModuleReference;

/**
 * Data access interface for external modules.
 */
public interface ExternalModuleDao {

    ExternalModule get(ModuleReference reference);

    int update(ExternalModule externalModule);

    int create(ExternalModule externalModule);
}
