package de.fh_bielefeld.newsboard.dao;

import de.fh_bielefeld.newsboard.model.ExternModule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 * Created by felixmeyer on 17.12.16.
 */
public class ExternModuleDaoImpl implements ExternModuleDao {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public ExternModuleDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public ExternModule getExternModuleWithId(String id) {
        return null;
    }

    @Override
    public int updateExternModule(ExternModule externModule) {
        return 0;
    }

    @Override
    public int insertExternModule(ExternModule externModule) {
        return 0;
    }
}
