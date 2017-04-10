package de.fhbielefeld.newsboard.dao.mysql;

import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Wrapper class to use a RowMapper as ResultSetExtractor.
 *
 * Other than the class included in Spring, it doesn't return a List, but instead the same type
 * as the wrapped RowMapper.
 */
class RowMapperResultSetExtractor<T> implements ResultSetExtractor<T> {

    private final RowMapper<T> rowMapper;

    RowMapperResultSetExtractor(RowMapper<T> rowMapper) {
        this.rowMapper = rowMapper;
    }

    @Override
    public T extractData(ResultSet rs) throws SQLException {
        if (rs.next()) {
            return rowMapper.mapRow(rs, 1);
        } else {
            return null;
        }
    }
}
