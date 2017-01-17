package de.fh_bielefeld.newsboard.dao.mysql;

import de.fh_bielefeld.newsboard.dao.DocumentDao;
import de.fh_bielefeld.newsboard.dao.ExternModuleDao;
import de.fh_bielefeld.newsboard.model.Document;
import de.fh_bielefeld.newsboard.model.DocumentMetaData;
import de.fh_bielefeld.newsboard.model.ExternalModule;
import de.fh_bielefeld.newsboard.model.Sentence;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import java.sql.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
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
            "UPDATE document SET title = ?, author = ?, source = ?, creation_time = ?, crawl_time = ?, module_id = ? WHERE id = ?";
    private static final String INSERT_DOCUMENT =
            "INSERT INTO document VALUES (null, ?, ?, ?, ?, ?, ?)";

    private JdbcTemplate jdbcTemplate;
    private SentenceDaoImpl sentenceDao;
    private ExternModuleDao externModuleDao;


    @Autowired
    public DocumentDaoImpl(JdbcTemplate jdbcTemplate, SentenceDaoImpl sentenceDao, ExternModuleDao externModuleDao) {
        this.jdbcTemplate = jdbcTemplate;
        this.sentenceDao = sentenceDao;
        this.externModuleDao = externModuleDao;
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
        final String moduleId = document.getModule() == null ? null : document.getModule().getId();
        KeyHolder keyHolder = new GeneratedKeyHolder();
        int numRows = jdbcTemplate.update(new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                PreparedStatement pst =
                        connection.prepareStatement(INSERT_DOCUMENT, new String[]{"id"});
                pst.setString(1, document.getTitle());
                pst.setString(2, document.getAuthor());
                pst.setString(3, document.getSource());
                pst.setDate(4, new Date(document.getCreationTime().getTimeInMillis()));
                pst.setDate(5, new Date(document.getCrawlTime().getTimeInMillis()));
                pst.setString(6, moduleId);
                return pst;
            }
        }, keyHolder);

        document.setId(keyHolder.getKey().intValue());
        return numRows;
    }

    @Override
    public int insertDocumentWithSentences(Document document) {
        int rows = insertDocument(document);
        for (Sentence s : document.getSentences()) {
            rows += sentenceDao.insertSentence(s, document);
        }
        return rows;
    }

    private Document getDocumentFromDocumentDatabaseObject(DocumentDatabaseObject rawDocument) {
        if (rawDocument == null) {
            return null;
        } else {
            Document document = new Document();
            document.setId(rawDocument.getId());
            document.setMetaData(getDocumentMetaDataFromDocumentDatabaseObject(rawDocument));
            document.setSentences(getAllSentencesInDocument(document));
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

    private ExternalModule getExternModuleWithModuleId(String moduleId) {
        return externModuleDao.getExternModuleWithId(moduleId);
    }

    private List<Sentence> getAllSentencesInDocument(Document document) {
        return sentenceDao.getAllSentencesInDocument(document);
    }

    protected class DocumentDatabaseObjectRowMapper implements RowMapper<DocumentDatabaseObject> {
        @Override
        public DocumentDatabaseObject mapRow(ResultSet resultSet, int i) throws SQLException {
            DocumentDatabaseObject document = new DocumentDatabaseObject();
            document.setId(resultSet.getInt("id"));
            document.setAuthor(resultSet.getString("author"));
            document.setTitle(resultSet.getString("title"));
            document.setSource(resultSet.getString("source"));
            document.setModuleId(resultSet.getString("module_id"));
            document.setCrawlTime(getCalendarFromTime(resultSet.getDate("crawl_time")));
            document.setCreationTime(getCalendarFromTime(resultSet.getDate("creation_time")));
            return document;
        }

        private Calendar getCalendarFromTime(Date date) {
            if (date == null) {
                return null;
            } else {
                Calendar cal = new GregorianCalendar();
                cal.setTime(date);
                return cal;
            }
        }
    }

    protected class DocumentDatabaseObject {
        private Integer id;
        private String title;
        private String author;
        private String source;
        private String moduleId;
        private Calendar creationTime;
        private Calendar crawlTime;

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getAuthor() {
            return author;
        }

        public void setAuthor(String author) {
            this.author = author;
        }

        public String getSource() {
            return source;
        }

        public void setSource(String source) {
            this.source = source;
        }

        public Calendar getCreationTime() {
            return creationTime;
        }

        public void setCreationTime(Calendar creationTime) {
            this.creationTime = creationTime;
        }

        public Calendar getCrawlTime() {
            return crawlTime;
        }

        public void setCrawlTime(Calendar crawlTime) {
            this.crawlTime = crawlTime;
        }

        public String getModuleId() {
            return moduleId;
        }

        public void setModuleId(String moduleId) {
            this.moduleId = moduleId;
        }
    }
}
