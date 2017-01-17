package de.fh_bielefeld.newsboard.dao;

import de.fh_bielefeld.newsboard.model.ExternalModule;

/**
 * Created by felixmeyer on 11.12.16.
 */
public interface ExternModuleDao {
    public ExternalModule getExternModuleWithId(String id);
    public int updateExternModule(ExternalModule externalModule);
    public int insertExternModule(ExternalModule externalModule);
}
