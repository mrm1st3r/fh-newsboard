package de.fh_bielefeld.newsboard.dao;

import de.fh_bielefeld.newsboard.model.ExternDocument;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

/**
 * Created by felixmeyer on 17.12.16.
 */
public class ExternDocumentDaoImpl implements ExternDocumentDao {

    private JdbcTemplate jdbcTemplate;

    public ExternDocumentDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public ExternDocument getExternDocumentWithId(int id) {
        return null;
    }

    @Override
    public List<ExternDocument> getAllExternDocumentsWithoutData() {
        return null;
    }

    @Override
    public int updateExternDocument(ExternDocument externDocument) {
        return 0;
    }

    @Override
    public int insertExternDocument(ExternDocument externDocument) {
        return 0;
    }
}
