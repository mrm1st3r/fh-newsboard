package de.fh_bielefeld.newsboard.dao.mysql;

import de.fh_bielefeld.newsboard.dao.ExternModuleDao;
import de.fh_bielefeld.newsboard.model.ExternalModule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by felixmeyer on 17.12.16.
 */
@Component
public class ExternModuleDaoImpl implements ExternModuleDao {
    private static final String GET_MODULE_WITH_ID =
            "SELECT id, name, author, description FROM extern_module WHERE id = ?";
    private static final String UPDATE_MODULE =
            "UPDATE extern_module SET name = ?, author = ?, description = ? WHERE id = ?";
    private static final String INSERT_MODULE =
            "INSERT INTO extern_module(id, name, author, description) VALUES (?, ?, ?, ?)";

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public ExternModuleDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public ExternalModule getExternModuleWithId(String id) {
        return jdbcTemplate.queryForObject(GET_MODULE_WITH_ID, new ExternModuleRowMapper(), id);
    }

    @Override
    public int updateExternModule(ExternalModule externalModule) {
        Object[] attributes = {
                externalModule.getName(),
                externalModule.getAuthor(),
                externalModule.getDescription(),
                externalModule.getId()
        };
        return jdbcTemplate.update(UPDATE_MODULE, attributes);
    }

    @Override
    public int insertExternModule(ExternalModule externalModule) {
        Object[] attributes = {
                externalModule.getId(),
                externalModule.getName(),
                externalModule.getAuthor(),
                externalModule.getDescription()
        };
        return jdbcTemplate.update(INSERT_MODULE, attributes);
    }

    protected class ExternModuleRowMapper implements RowMapper<ExternalModule> {
        @Override
        public ExternalModule mapRow(ResultSet resultSet, int i) throws SQLException {
            ExternalModule externalModule = new ExternalModule(
                    resultSet.getString("id"),
                    resultSet.getString("name"),
                    resultSet.getString("author"),
                    resultSet.getString("description")
            );

            return externalModule;
        }
    }
}
