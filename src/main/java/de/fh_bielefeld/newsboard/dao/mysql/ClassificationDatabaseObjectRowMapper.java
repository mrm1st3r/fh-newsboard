package de.fh_bielefeld.newsboard.dao.mysql;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by felixmeyer on 02.01.17.
 */
class ClassificationDatabaseObjectRowMapper implements RowMapper<ClassificationDatabaseObject> {

    @Override
    public ClassificationDatabaseObject mapRow(ResultSet resultSet, int i) throws SQLException {
        ClassificationDatabaseObject rawClassification = new ClassificationDatabaseObject();

        rawClassification.setSentId(resultSet.getInt("sent_id"));
        rawClassification.setDocumentId(resultSet.getInt("document_id"));
        rawClassification.setModuleId(resultSet.getString("module_id"));
        rawClassification.setValue(resultSet.getDouble("value"));
        rawClassification.setConfidence(resultSet.getDouble("confidence"));

        return rawClassification;
    }
}
