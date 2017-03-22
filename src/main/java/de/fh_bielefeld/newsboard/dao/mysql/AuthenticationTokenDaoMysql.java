package de.fh_bielefeld.newsboard.dao.mysql;

import de.fh_bielefeld.newsboard.dao.AuthenticationTokenDao;
import de.fh_bielefeld.newsboard.model.AuthenticationToken;
import de.fh_bielefeld.newsboard.model.ExternalModule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import java.sql.PreparedStatement;
import java.util.List;
import java.util.Optional;

/**
 * MySQL implementation for authentication token DAO.
 */
@Component
public class AuthenticationTokenDaoMysql implements AuthenticationTokenDao {

    private static final String GET_TOKEN_WITH_ID =
            "SELECT id, module_id, token FROM authentication_token WHERE id = ?";
    private static final String FIND_TOKEN =
            "SELECT id, module_id, token FROM authentication_token WHERE token = ?";
    private static final String GET_ALL_TOKEN_FOR_MODULE =
            "SELECT id, module_id, token FROM authentication_token WHERE module_id = ?";
    private static final String UPDATE_TOKEN =
            "UPDATE authentication_token SET token = ?, module_id = ? WHERE id = ?";
    private static final String INSERT_TOKEN =
            "INSERT INTO authentication_token (token, module_id) VALUES (?, ?)";

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public AuthenticationTokenDaoMysql(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public AuthenticationToken get(int id) {
        return jdbcTemplate.query(GET_TOKEN_WITH_ID, new RowMapperResultSetExtractor<>(rowMapper), id);
    }

    @Override
    public List<AuthenticationToken> findForModule(ExternalModule externalModule) {
        return jdbcTemplate.query(GET_ALL_TOKEN_FOR_MODULE, rowMapper, externalModule.getId());
    }

    @Override
    public int update(AuthenticationToken authToken) {
        return jdbcTemplate.update(UPDATE_TOKEN, authToken.getToken(), authToken.getModuleId(), authToken.getId());
    }

    @Override
    public int create(AuthenticationToken authToken) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        int numRows = jdbcTemplate.update(connection -> {
            PreparedStatement pst = connection.prepareStatement(INSERT_TOKEN, new String[]{"id"});
            pst.setString(1, authToken.getToken());
            pst.setString(2, authToken.getModuleId());
            return pst;
        }, keyHolder);
        authToken.setId(keyHolder.getKey().intValue());
        return numRows;
    }

    @Override
    public Optional<AuthenticationToken> find(String token) {
        AuthenticationToken tok = jdbcTemplate.query(FIND_TOKEN, new RowMapperResultSetExtractor<>(rowMapper), token);
        if (tok == null) {
            return Optional.empty();
        }
        return Optional.of(tok);
    }

    private final RowMapper<AuthenticationToken> rowMapper = (resultSet, i) -> new AuthenticationToken(
            resultSet.getInt("id"),
            resultSet.getString("module_id"),
            resultSet.getString("token"));
}
