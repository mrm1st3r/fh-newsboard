package de.fhbielefeld.newsboard.dao.mysql;

import de.fhbielefeld.newsboard.model.access.AccessId;
import de.fhbielefeld.newsboard.model.module.ExternalModule;
import de.fhbielefeld.newsboard.model.module.ExternalModuleDao;
import de.fhbielefeld.newsboard.model.module.ModuleReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

/**
 * MySQL implementation for ExternalModule DAO.
 */
@Component
public class ExternalModuleDaoMysql implements ExternalModuleDao {

    private static final String GET_MODULE_WITH_ID =
            "SELECT module_id, title, author, description, access_id FROM module WHERE module_id = ?";
    private static final String UPDATE_MODULE =
            "UPDATE module SET title = ?, author = ?, description = ?, access_id = ? WHERE module_id = ?";
    private static final String INSERT_MODULE =
            "INSERT INTO module(title, author, description, access_id, module_id) VALUES (?, ?, ?, ?, ?)";

    private final RowMapper<ExternalModule> rowMapper = (resultSet, i) -> new ExternalModule(
            resultSet.getString("module_id"),
            resultSet.getString("title"),
            resultSet.getString("author"),
            resultSet.getString("description"),
            new AccessId(resultSet.getString("access_id")));

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public ExternalModuleDaoMysql(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public ExternalModule get(ModuleReference reference) {
        return jdbcTemplate.query(GET_MODULE_WITH_ID, new RowMapperResultSetExtractor<>(rowMapper), reference.getId());
    }

    @Override
    public int update(ExternalModule externalModule) {
        return jdbcTemplate.update(UPDATE_MODULE, makeAttributes(externalModule));
    }

    @Override
    public int create(ExternalModule externalModule) {
        return jdbcTemplate.update(INSERT_MODULE, makeAttributes(externalModule));
    }

    private Object[] makeAttributes(ExternalModule module) {
        return new Object[] {module.getName(), module.getAuthor(), module.getDescription(), module.getAccessReference().raw(), module.getId()};
    }
}
