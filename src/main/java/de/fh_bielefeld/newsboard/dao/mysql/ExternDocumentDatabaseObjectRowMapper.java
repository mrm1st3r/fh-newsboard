package de.fh_bielefeld.newsboard.dao.mysql;

import de.fh_bielefeld.newsboard.model.ExternDocument;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by felixmeyer on 03.01.17.
 */
class ExternDocumentDatabaseObjectRowMapper implements RowMapper<ExternDocumentDatabaseObject> {
    @Override
    public ExternDocumentDatabaseObject mapRow(ResultSet resultSet, int i) throws SQLException {
        ExternDocumentDatabaseObject externDocument = new ExternDocumentDatabaseObject();

        externDocument.setId(resultSet.getInt("id"));
        externDocument.setTitle(resultSet.getString("title"));
        externDocument.setHtml(resultSet.getString("html"));
        externDocument.setModuleId(resultSet.getString("module_id"));

        return externDocument;
    }
}
