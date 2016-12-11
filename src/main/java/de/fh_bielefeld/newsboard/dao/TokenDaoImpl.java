package de.fh_bielefeld.newsboard.dao;

import de.fh_bielefeld.newsboard.model.Token;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by felixmeyer on 11.12.16.
 */
public class TokenDaoImpl implements TokenDao {
    private static final String GET_TOKEN_WITH_ID = "SELECT id, module_id, token FROM token WHERE id = ?";
    private static final String GET_ALL_TOKEN_FOR_MODULE = "SELECT id, module_id, token FROM token WHERE module_id = ?";

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public Token getTokenWithId(int id) {
        return jdbcTemplate.queryForObject(GET_TOKEN_WITH_ID, new TokenRowMapper(), id);
    }

    @Override
    public List<Token> getAllTokenForModule(String moduleId) {
        return jdbcTemplate.query(GET_ALL_TOKEN_FOR_MODULE, new TokenRowMapper(), moduleId);
    }

    private class TokenRowMapper implements RowMapper<Token> {
        @Override
        public Token mapRow(ResultSet resultSet, int i) throws SQLException {
            Token token = new Token(
                    resultSet.getInt("id"),
                    resultSet.getString("module_id"),
                    resultSet.getString("token"));
            return token;
        }
    }
}
