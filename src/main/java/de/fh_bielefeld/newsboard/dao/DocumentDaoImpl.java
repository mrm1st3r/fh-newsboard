package de.fh_bielefeld.newsboard.dao;

import de.fh_bielefeld.newsboard.model.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 * Created by felixmeyer on 17.12.16.
 */
public class DocumentDaoImpl implements DocumentDao {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public DocumentDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Document getDocumentWithId(int id) {
        return null;
    }

    @Override
    public int updateDocument(Document document) {
        return 0;
    }

    @Override
    public int insertDocument(Document document) {
        return 0;
    }
}
