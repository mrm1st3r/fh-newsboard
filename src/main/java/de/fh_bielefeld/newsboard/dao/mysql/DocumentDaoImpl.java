package de.fh_bielefeld.newsboard.dao.mysql;

import de.fh_bielefeld.newsboard.dao.ClassificationDao;
import de.fh_bielefeld.newsboard.dao.DocumentDao;
import de.fh_bielefeld.newsboard.model.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by felixmeyer on 17.12.16.
 */
@Component
public class DocumentDaoImpl implements DocumentDao {
    private static final String GET_DOCUMENT_WITH_ID =
            "SELECT * FROM document WHERE id = ?";
    private static final String GET_DOCUMENTS_ONLY_METADATA =
            "SELECT * FROM document";
    private static final String UPDATE_DOCUMENT =
            "UPDATE DOCUMENT SET title = ?, author = ?, source = ?, creation_time = ?, crawl_time = ?, module_id = ? WHERE id = ?";
    private static final String INSERT_DOCUMENT =
            "INSERT INTO DOCUMENT VALUES (?, ?, ?, ?, ?, ?)";

    private JdbcTemplate jdbcTemplate;
    private SentenceDaoImpl sentenceDao;
    private ClassificationDao classificationDao;

    @Autowired
    public DocumentDaoImpl(JdbcTemplate jdbcTemplate, SentenceDaoImpl sentenceDao, ClassificationDaoImpl classificationDao) {
        this.jdbcTemplate = jdbcTemplate;
        this.sentenceDao = sentenceDao;
        this.classificationDao = classificationDao;
    }

    @Override
    public Document getDocumentWithId(int id) {
        Object[] attributes = {
                id
        };

        DocumentDatabaseObject rawDocument = jdbcTemplate.queryForObject(
                GET_DOCUMENT_WITH_ID,
                new DocumentDatabaseObjectRowMapper(),
                attributes);


        return null;
    }

    @Override
    public List<Document> getAllDocumentsOnlyWithMetaData() {
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
