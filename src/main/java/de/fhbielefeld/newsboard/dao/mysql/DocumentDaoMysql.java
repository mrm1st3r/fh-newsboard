package de.fhbielefeld.newsboard.dao.mysql;

import de.fhbielefeld.newsboard.model.document.*;
import de.fhbielefeld.newsboard.model.module.ModuleId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import static org.springframework.util.Assert.notNull;

/**
 * MySQL implementation for document DAO.
 */
@Component
public class DocumentDaoMysql implements DocumentDao {
    private static final String GET_DOCUMENT_WITH_ID = "SELECT * FROM document WHERE document_id = ?";
    private static final String GET_DOCUMENT_LIST = "SELECT * FROM document ORDER BY crawl_time DESC LIMIT ?";
    private static final String INSERT_DOCUMENT = "INSERT INTO document VALUES (null, ?, ?, ?, ?, ?, ?)";
    private static final String GET_UNCLASSIFIED_DOCUMENTS_FOR_EXTERNAL_MODULE =
            "SELECT * FROM document WHERE document_id NOT IN(SELECT document_id FROM sentence WHERE sentence_id IN " +
                    "(SELECT sentence_id FROM classification WHERE module_id = ?))";

    private final JdbcTemplate jdbcTemplate;
    private SentenceDao sentenceDao;

    private final RowMapper<Document> documentMapper = (r, i) -> {
        DocumentId id = new DocumentId(r.getInt("document_id"));
        DocumentMetaData meta = new DocumentMetaData(
                r.getString("title"),
                r.getString("author"), r.getString("source_url"),
                getCalendarFromTime(r.getDate("creation_time")),
                getCalendarFromTime(r.getDate("crawl_time")),
                new ModuleId(r.getString("module_id"))
        );
        return new Document(id, meta, sentenceDao.findForDocument(id));
    };

    @Autowired
    public DocumentDaoMysql(JdbcTemplate jdbcTemplate, SentenceDao sentenceDao) {
        this.jdbcTemplate = jdbcTemplate;
        this.sentenceDao = sentenceDao;
    }

    @Override
    public Document get(DocumentId id) {
        return jdbcTemplate.query(GET_DOCUMENT_WITH_ID, new RowMapperResultSetExtractor<>(documentMapper), id.raw());
    }

    @Override
    public Document create(Document document) {
        DocumentMetaData metaData = document.getMetaData();
        notNull(metaData.getModule());
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement pst = connection.prepareStatement(INSERT_DOCUMENT, new String[]{"document_id"});
            pst.setString(1, metaData.getTitle());
            pst.setString(2, metaData.getAuthor());
            pst.setString(3, metaData.getSource());
            pst.setDate(4, new Date(metaData.getCreationTime().getTimeInMillis()));
            pst.setDate(5, new Date(metaData.getCrawlTime().getTimeInMillis()));
            pst.setString(6, metaData.getModule().raw());
            return pst;
        }, keyHolder);
        Document identifiedDocument = document.copyForId(keyHolder.getKey().intValue());
        for (Sentence s : document.getSentences()) {
            sentenceDao.create(s, identifiedDocument);
        }
        return identifiedDocument;
    }

    @Override
    public List<Document> findLatest(int maximumAmount) {
        return jdbcTemplate.query(GET_DOCUMENT_LIST, documentMapper, maximumAmount);
    }

    @Override
    public List<Document> findUnclassifiedForModule(ModuleId module) {
        return jdbcTemplate.query(GET_UNCLASSIFIED_DOCUMENTS_FOR_EXTERNAL_MODULE, documentMapper, module.raw());
    }

    private Calendar getCalendarFromTime(Date date) {
        if (date == null) {
            return null;
        }
        Calendar cal = new GregorianCalendar();
        cal.setTime(date);
        return cal;
    }
}
