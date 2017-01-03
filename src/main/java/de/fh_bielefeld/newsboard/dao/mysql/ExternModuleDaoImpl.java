package de.fh_bielefeld.newsboard.dao.mysql;

import de.fh_bielefeld.newsboard.dao.ExternModuleDao;
import de.fh_bielefeld.newsboard.model.ExternModule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

/**
 * Created by felixmeyer on 17.12.16.
 */
@Component
public class ExternModuleDaoImpl implements ExternModuleDao {
    private static final String GET_MODULE_WITH_ID =
            "SELECT id, name, author, description FROM extern_module WHERE id = ?";
    private static final String UPDATE_MODULE =
            "UPDATE TABLE extern_module SET name = ?, author = ?, description = ? WHERE id = ?";
    private static final String INSERT_MODULE =
            "INSERT INTO extern_module(name, author, description) VALUES (?, ?, ?)";

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public ExternModuleDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public ExternModule getExternModuleWithId(String id) {
        return jdbcTemplate.queryForObject(GET_MODULE_WITH_ID, new ExternModuleRowMapper(), id);
    }

    @Override
    public int updateExternModule(ExternModule externModule) {
        Object args[] = {
                externModule.getName(),
                externModule.getAuthor(),
                externModule.getDescription(),
                externModule.getId()
        };
        return jdbcTemplate.update(UPDATE_MODULE, args);
    }

    @Override
    public int insertExternModule(ExternModule externModule) {
        Object args[] = {
                externModule.getName(),
                externModule.getAuthor(),
                externModule.getDescription()
        };
        return jdbcTemplate.update(INSERT_MODULE, args);
    }

}
