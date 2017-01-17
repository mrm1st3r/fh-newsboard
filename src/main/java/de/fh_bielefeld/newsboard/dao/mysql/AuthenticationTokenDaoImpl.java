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
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

/**
 * MySQL implementation for authentication token DAO.
 */
@Component
public class AuthenticationTokenDaoImpl implements AuthenticationTokenDao {
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
    public AuthenticationTokenDaoImpl (JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public AuthenticationToken getTokenWithId(int id) {
        return jdbcTemplate.queryForObject(GET_TOKEN_WITH_ID, new AuthenticationTokenRowMapper(), id);
    }

    @Override
    public List<AuthenticationToken> getAllTokenForModule(ExternalModule externalModule) {
        return jdbcTemplate.query(GET_ALL_TOKEN_FOR_MODULE, new AuthenticationTokenRowMapper(), externalModule.getId());
    }

    @Override
    public int updateAuthenticationToken(AuthenticationToken authToken) {
        Object[] values = {authToken.getToken(), authToken.getModuleId(), authToken.getId()};
        return jdbcTemplate.update(UPDATE_TOKEN, values);
    }

    @Override
    public int insertAuthenticationToken(AuthenticationToken authToken) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        int numRows = jdbcTemplate.update(connection -> {
            PreparedStatement pst =
                    connection.prepareStatement(INSERT_TOKEN, new String[]{"id"});
            pst.setString(1, authToken.getToken());
            pst.setString(2, authToken.getModuleId());
            return pst;
        }, keyHolder);
        authToken.setId(keyHolder.getKey().intValue());
        return numRows;
    }

    @Override
    public Optional<AuthenticationToken> findToken(String token) {
        List<AuthenticationToken> tokens = jdbcTemplate.query(FIND_TOKEN, new AuthenticationTokenRowMapper(), token);
        if (tokens.size() == 0) {
            return Optional.empty();
        } else {
            return Optional.of(tokens.get(0));
        }
    }

    private class AuthenticationTokenRowMapper implements RowMapper<AuthenticationToken> {
        @Override
        public AuthenticationToken mapRow(ResultSet resultSet, int i) throws SQLException {
            return new AuthenticationToken(
                    resultSet.getInt("id"),
                    resultSet.getString("module_id"),
                    resultSet.getString("token"));
        }
    }
}
