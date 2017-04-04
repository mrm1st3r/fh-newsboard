package de.fh_bielefeld.newsboard.dao.mysql;

import de.fh_bielefeld.newsboard.dao.ExternalModuleDao;
import de.fh_bielefeld.newsboard.model.AccessReference;
import de.fh_bielefeld.newsboard.model.ExternalModule;
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

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public ExternalModuleDaoMysql(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public ExternalModule get(String id) {
        return jdbcTemplate.query(GET_MODULE_WITH_ID, new RowMapperResultSetExtractor<>(rowMapper), id);
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
        return new Object[] {module.getName(), module.getAuthor(), module.getDescription(), module.getAccessReference().getId(), module.getId()};
    }

    private final RowMapper<ExternalModule> rowMapper = (resultSet, i) -> new ExternalModule(
            resultSet.getString("module_id"),
            resultSet.getString("title"),
            resultSet.getString("author"),
            resultSet.getString("description"),
            new AccessReference(resultSet.getString("access_id")));
}
