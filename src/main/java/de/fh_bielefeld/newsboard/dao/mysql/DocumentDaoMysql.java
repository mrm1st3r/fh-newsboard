package de.fh_bielefeld.newsboard.dao.mysql;

import de.fh_bielefeld.newsboard.dao.DocumentDao;
import de.fh_bielefeld.newsboard.dao.ExternalModuleDao;
import de.fh_bielefeld.newsboard.dao.SentenceDao;
import de.fh_bielefeld.newsboard.model.Document;
import de.fh_bielefeld.newsboard.model.DocumentMetaData;
import de.fh_bielefeld.newsboard.model.Sentence;
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
    private static final String GET_DOCUMENTS_ONLY_METADATA = "SELECT * FROM document";
    private static final String UPDATE_DOCUMENT =
            "UPDATE document SET title = ?, author = ?, source_url = ?, creation_time = ?, crawl_time = ?, module_id = ? WHERE document_id = ?";
    private static final String INSERT_DOCUMENT = "INSERT INTO document VALUES (null, ?, ?, ?, ?, ?, ?)";
    private static final String GET_UNCLASSIFIED_DOCUMENTS_FOR_EXTERNAL_MODULE =
            "SELECT * FROM document WHERE document_id NOT IN(SELECT document_id FROM sentence WHERE sentence_id IN " +
                    "(SELECT sentence_id FROM classification WHERE module_id = ?))";

    private JdbcTemplate jdbcTemplate;
    private SentenceDao sentenceDao;
    private ExternalModuleDao externalModuleDao;

    @Autowired
    public DocumentDaoMysql(JdbcTemplate jdbcTemplate, SentenceDao sentenceDao, ExternalModuleDao externalModuleDao) {
        this.jdbcTemplate = jdbcTemplate;
        this.sentenceDao = sentenceDao;
        this.externalModuleDao = externalModuleDao;
    }

    @Override
    public Document get(int id) {
        return jdbcTemplate.query(GET_DOCUMENT_WITH_ID, new RowMapperResultSetExtractor<>(documentMapper), id);
    }

    @Override
    public List<Document> findAllStubs() {
        return jdbcTemplate.query(GET_DOCUMENTS_ONLY_METADATA, stubMapper);
    }

    @Override
    public int update(Document document) {
        notNull(document.getModule());
        return jdbcTemplate.update(UPDATE_DOCUMENT, document.getTitle(), document.getAuthor(), document.getSource(),
                document.getCreationTime(), document.getCrawlTime(), document.getModule().getId(), document.getId());
    }

    @Override
    public int create(Document document) {
        notNull(document.getModule());
        KeyHolder keyHolder = new GeneratedKeyHolder();
        int numRows = jdbcTemplate.update(connection -> {
            PreparedStatement pst = connection.prepareStatement(INSERT_DOCUMENT, new String[]{"document_id"});
            pst.setString(1, document.getTitle());
            pst.setString(2, document.getAuthor());
            pst.setString(3, document.getSource());
            pst.setDate(4, new Date(document.getCreationTime().getTimeInMillis()));
            pst.setDate(5, new Date(document.getCrawlTime().getTimeInMillis()));
            pst.setString(6, document.getModule().getId());
            return pst;
        }, keyHolder);
        document.setId(keyHolder.getKey().intValue());
        for (Sentence s : document.getSentences()) {
            numRows += sentenceDao.create(s, document);
        }
        return numRows;
    }

    @Override
    public List<Document> findUnclassifiedForModule(String externalModuleId) {
        return jdbcTemplate.query(GET_UNCLASSIFIED_DOCUMENTS_FOR_EXTERNAL_MODULE, documentMapper, externalModuleId);
    }

    private Calendar getCalendarFromTime(Date date) {
        if (date == null) {
            return null;
        }
        Calendar cal = new GregorianCalendar();
        cal.setTime(date);
        return cal;
    }

    private final RowMapper<Document> stubMapper = (r, i) -> {
        Document doc = new Document();
        doc.setId(r.getInt("document_id"));

        DocumentMetaData meta = new DocumentMetaData(r.getString("title"),
                r.getString("author"), r.getString("source_url"),
                getCalendarFromTime(r.getDate("crawl_time")), getCalendarFromTime(r.getDate("creation_time")),
                externalModuleDao.get(r.getString("module_id")));

        doc.setMetaData(meta);
        return doc;
    };

    private final RowMapper<Document> documentMapper = (resultSet, i) -> {
        Document doc = stubMapper.mapRow(resultSet, i);
        doc.setSentences(sentenceDao.findForDocument(doc));
        return doc;
    };
}
