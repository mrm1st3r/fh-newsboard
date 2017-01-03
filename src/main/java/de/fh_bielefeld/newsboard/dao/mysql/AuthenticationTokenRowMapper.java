package de.fh_bielefeld.newsboard.dao.mysql;

import de.fh_bielefeld.newsboard.model.AuthenticationToken;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by felixmeyer on 02.01.17.
 */
class AuthenticationTokenRowMapper implements RowMapper<AuthenticationToken> {
    @Override
    public AuthenticationToken mapRow(ResultSet resultSet, int i) throws SQLException {
        AuthenticationToken token = new AuthenticationToken(
                resultSet.getInt("id"),
                resultSet.getString("module_id"),
                resultSet.getString("token"));
        return token;
    }
}
