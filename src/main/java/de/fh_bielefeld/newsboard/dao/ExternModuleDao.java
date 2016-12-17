package de.fh_bielefeld.newsboard.dao;

import de.fh_bielefeld.newsboard.model.ExternModule;

/**
 * Created by felixmeyer on 11.12.16.
 */
public interface ExternModuleDao {
    public ExternModule getExternModuleWithId(String id);
    public int updateExternModule(ExternModule externModule);
    public int insertExternModule(ExternModule externModule);
}
