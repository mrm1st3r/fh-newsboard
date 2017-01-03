package de.fh_bielefeld.newsboard.dao.mysql;

import de.fh_bielefeld.newsboard.dao.ClassificationDao;
import de.fh_bielefeld.newsboard.dao.DocumentDao;
import de.fh_bielefeld.newsboard.dao.ExternModuleDao;
import de.fh_bielefeld.newsboard.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
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
    private ExternModuleDao externModuleDao;


    @Autowired
    public DocumentDaoImpl(JdbcTemplate jdbcTemplate, SentenceDaoImpl sentenceDao, ClassificationDaoImpl classificationDao, ExternModuleDao externModuleDao) {
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

        return getDocumentFromDocumentDatabaseObject(rawDocument);
    }

    @Override
    public List<Document> getAllDocumentsOnlyWithMetaData() {
        List<DocumentDatabaseObject> rawDocuments = jdbcTemplate.query(
                GET_DOCUMENTS_ONLY_METADATA,
                new DocumentDatabaseObjectRowMapper());

        List<Document> allDocumentsWithOnlyMetaData = new ArrayList<>();
        for (DocumentDatabaseObject rawDocument : rawDocuments) {
            Document document = new Document();
            document.setId(rawDocument.getId());
            document.setMetaData(getDocumentMetaDataFromDocumentDatabaseObject(rawDocument));
            allDocumentsWithOnlyMetaData.add(document);
        }

        return allDocumentsWithOnlyMetaData;
    }

    @Override
    public int updateDocument(Document document) {
        String moduleId = document.getModule() == null ? null : document.getModule().getId();
        Object[] attributes = {
                document.getTitle(),
                document.getAuthor(),
                document.getSource(),
                document.getCreationTime(),
                document.getCrawlTime(),
                moduleId,
                document.getId()
        };

        return jdbcTemplate.update(UPDATE_DOCUMENT, attributes);
    }

    @Override
    public int insertDocument(Document document) {
        String moduleId = document.getModule() == null ? null : document.getModule().getId();
        Object[] attributes = {
                document.getTitle(),
                document.getAuthor(),
                document.getSource(),
                document.getCreationTime(),
                document.getCrawlTime(),
                moduleId
        };

        return jdbcTemplate.update(UPDATE_DOCUMENT, attributes);
    }

    private Document getDocumentFromDocumentDatabaseObject(DocumentDatabaseObject rawDocument) {
        if (rawDocument == null) {
            return null;
        } else {
            Document document = new Document();
            document.setId(rawDocument.getId());
            document.setMetaData(getDocumentMetaDataFromDocumentDatabaseObject(rawDocument));
            document.setSentences(getAllSentencesInDocument(document));
            document.setClassifications(getAllClassificationsForDocument(document));
            return document;
        }
    }

    private DocumentMetaData getDocumentMetaDataFromDocumentDatabaseObject(DocumentDatabaseObject rawDocument) {
        DocumentMetaData documentMetaData = new DocumentMetaData();
        documentMetaData.setAuthor(rawDocument.getAuthor());
        documentMetaData.setSource(rawDocument.getSource());
        documentMetaData.setTitle(rawDocument.getTitle());
        documentMetaData.setCrawlTime(rawDocument.getCrawlTime());
        documentMetaData.setCreationTime(rawDocument.getCreationTime());
        documentMetaData.setModule(getExternModuleWithModuleId(rawDocument.getModuleId()));
        return documentMetaData;
    }

    private ExternModule getExternModuleWithModuleId(String moduleId) {
        return externModuleDao.getExternModuleWithId(moduleId);
    }

    private List<Classification> getAllClassificationsForDocument(Document document) {
        return classificationDao.getAllClassificationsForDocument(document);
    }

    private List<Sentence> getAllSentencesInDocument(Document document) {
        return sentenceDao.getAllSentencesInDocument(document);
    }
}
