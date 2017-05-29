package de.fhbielefeld.newsboard.model.module;

/**
 * Data access interface for external modules.
 */
public interface ExternalModuleDao {

    ExternalModule get(ModuleReference reference);

    int update(ExternalModule externalModule);

    int create(ExternalModule externalModule);
}
