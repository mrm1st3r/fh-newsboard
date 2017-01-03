package de.fh_bielefeld.newsboard.dao.mysql;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by felixmeyer on 03.01.17.
 */
class SentenceDatabaseObjectRowMapper implements RowMapper<SentenceDatabaseObject> {
    @Override
    public SentenceDatabaseObject mapRow(ResultSet resultSet, int i) throws SQLException {
        SentenceDatabaseObject sentence = new SentenceDatabaseObject();
        sentence.setId(resultSet.getInt("id"));
        sentence.setNumber(resultSet.getInt("number"));
        sentence.setText(resultSet.getString("text"));
        sentence.setDocumentId(resultSet.getInt("document_id"));
        return sentence;
    }
}
