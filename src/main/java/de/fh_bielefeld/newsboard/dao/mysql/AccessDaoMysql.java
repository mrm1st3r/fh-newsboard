package de.fh_bielefeld.newsboard.dao.mysql;

import de.fh_bielefeld.newsboard.dao.AccessDao;
import de.fh_bielefeld.newsboard.model.Access;
import de.fh_bielefeld.newsboard.model.AccessRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

/**
 * MySQL implementation for authentication token DAO.
 */
@Component
public class AccessDaoMysql implements AccessDao {

    private static final String READ_QUERY =
            "SELECT access_id, role_id, passphrase, hash_type, enabled FROM access WHERE access_id = ?";
    private static final String UPDATE_QUERY =
            "UPDATE access SET passphrase = ?, hash_type = ?, enabled = ? WHERE access_id = ?";
    private static final String CREATE_QUERY =
            "INSERT INTO access (access_id, role_id, passphrase, hash_type, enabled) VALUES (?, ?, ?, ?, ?)";

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public AccessDaoMysql(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Access get(String id) {
        return jdbcTemplate.query(READ_QUERY, new RowMapperResultSetExtractor<>(rowMapper), id);
    }

    @Override
    public int update(Access access) {
        return jdbcTemplate.update(UPDATE_QUERY,
                access.getPassphrase(),
                access.getHashType(),
                access.isEnabled(),
                access.getId());
    }

    @Override
    public int create(Access access) {
        return jdbcTemplate.update(CREATE_QUERY,
                access.getId(),
                access.getRole().getRole(),
                access.getPassphrase(),
                access.getHashType(),
                access.isEnabled());
    }

    private final RowMapper<Access> rowMapper = (resultSet, i) -> new Access(
            resultSet.getString("access_id"),
            new AccessRole(resultSet.getString("role_id")),
            resultSet.getString("passphrase"),
            resultSet.getString("hash_type"),
            resultSet.getBoolean("enabled"));
}
