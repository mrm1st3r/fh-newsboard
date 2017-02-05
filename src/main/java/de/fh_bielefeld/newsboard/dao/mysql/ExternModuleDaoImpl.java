package de.fh_bielefeld.newsboard.dao.mysql;

import de.fh_bielefeld.newsboard.dao.ExternModuleDao;
import de.fh_bielefeld.newsboard.model.ExternalModule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

/**
 * MySQL implementation for ExternalModule DAO.
 */
@Component
public class ExternModuleDaoImpl implements ExternModuleDao {

    private static final String GET_MODULE_WITH_ID =
            "SELECT id, name, author, description FROM extern_module WHERE id = ?";
    private static final String UPDATE_MODULE =
            "UPDATE extern_module SET name = ?, author = ?, description = ? WHERE id = ?";
    private static final String INSERT_MODULE =
            "INSERT INTO extern_module(name, author, description, id) VALUES (?, ?, ?, ?)";

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public ExternModuleDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public ExternalModule getExternModuleWithId(String id) {
        return jdbcTemplate.query(GET_MODULE_WITH_ID, new RowMapperResultSetExtractor<>(rowMapper), id);
    }

    @Override
    public int updateExternModule(ExternalModule externalModule) {
        return jdbcTemplate.update(UPDATE_MODULE, makeAttributes(externalModule));
    }

    @Override
    public int insertExternModule(ExternalModule externalModule) {
        return jdbcTemplate.update(INSERT_MODULE, makeAttributes(externalModule));
    }

    private Object[] makeAttributes(ExternalModule module) {
        return new Object[] {module.getName(), module.getAuthor(), module.getDescription(), module.getId()};
    }

    private final RowMapper<ExternalModule> rowMapper = (resultSet, i) -> new ExternalModule(
            resultSet.getString("id"),
            resultSet.getString("name"),
            resultSet.getString("author"),
            resultSet.getString("description")
    );
}
