package de.fh_bielefeld.newsboard.dao.mysql;

import de.fh_bielefeld.newsboard.model.ExternModule;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by felixmeyer on 02.01.17.
 */
class ExternModuleRowMapper implements RowMapper<ExternModule> {
    @Override
    public ExternModule mapRow(ResultSet resultSet, int i) throws SQLException {
        ExternModule externModule = new ExternModule(
                resultSet.getString("id"),
                resultSet.getString("name"),
                resultSet.getString("author"),
                resultSet.getString("description")
        );

        return externModule;
    }
}
