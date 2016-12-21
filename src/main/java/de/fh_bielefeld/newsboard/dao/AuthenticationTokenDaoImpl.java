package de.fh_bielefeld.newsboard.dao;

import de.fh_bielefeld.newsboard.model.AuthenticationToken;
import de.fh_bielefeld.newsboard.model.ExternModule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by felixmeyer on 11.12.16.
 */
@Component
public class AuthenticationTokenDaoImpl implements AuthenticationTokenDao {
    private static final String GET_TOKEN_WITH_ID =
            "SELECT id, module_id, token FROM token WHERE id = ?";
    private static final String GET_ALL_TOKEN_FOR_MODULE =
            "SELECT id, module_id, token FROM token WHERE module_id = ?";
    private static final String UPDATE_TOKEN =
            "UPDATE authentication_token SET id = ?, token = ?, module_id = ? WHERE id = ?";
    private static final String INSERT_TOKEN =
            "INSERT INTO authentication_token (token, moduleId) VALUES (?, ?)";

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public AuthenticationTokenDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public AuthenticationToken getTokenWithId(int id) {
        return jdbcTemplate.queryForObject(GET_TOKEN_WITH_ID, new AuthenticationTokenRowMapper(), id);
    }

    @Override
    public List<AuthenticationToken> getAllTokenForModule(ExternModule externModule) {
        return jdbcTemplate.query(GET_ALL_TOKEN_FOR_MODULE, new AuthenticationTokenRowMapper(), externModule.getId());
    }

    @Override
    public int updateAuthenticationToken(AuthenticationToken authToken) {
        Object[] values = {
                authToken.getId(), authToken.getToken(), authToken.getModuleId()
        };
        return jdbcTemplate.update(UPDATE_TOKEN, values);
    }

    @Override
    public int insertAuthenticationToken(AuthenticationToken authToken) {
        Object[] values = {
                authToken.getToken(), authToken.getModuleId()
        };
        return jdbcTemplate.update(INSERT_TOKEN, values);
    }

    protected class AuthenticationTokenRowMapper implements RowMapper<AuthenticationToken> {
        @Override
        public AuthenticationToken mapRow(ResultSet resultSet, int i) throws SQLException {
            AuthenticationToken token = new AuthenticationToken(
                    resultSet.getInt("id"),
                    resultSet.getString("module_id"),
                    resultSet.getString("token"));
            return token;
        }
    }
}
