package de.fh_bielefeld.newsboard.dao.mysql;

import de.fh_bielefeld.newsboard.dao.DocumentDao;
import de.fh_bielefeld.newsboard.dao.ExternalModuleDao;
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
public class DocumentDaoImpl implements DocumentDao {
    private static final String GET_DOCUMENT_WITH_ID = "SELECT * FROM document WHERE id = ?";
    private static final String GET_DOCUMENTS_ONLY_METADATA = "SELECT * FROM document";
    private static final String UPDATE_DOCUMENT =
            "UPDATE document SET title = ?, author = ?, source = ?, creation_time = ?, crawl_time = ?, module_id = ? WHERE id = ?";
    private static final String INSERT_DOCUMENT = "INSERT INTO document VALUES (null, ?, ?, ?, ?, ?, ?)";
    private static final String GET_UNCLASSIFIED_DOCUMENTS_FOR_EXTERNAL_MODULE =
            "SELECT * FROM document WHERE id NOT IN(SELECT document_id FROM sentence WHERE id IN " +
                    "(SELECT sent_id FROM classification WHERE module_id = ?))";

    private JdbcTemplate jdbcTemplate;
    private SentenceDaoImpl sentenceDao;
    private ExternalModuleDao externalModuleDao;

    @Autowired
    public DocumentDaoImpl(JdbcTemplate jdbcTemplate, SentenceDaoImpl sentenceDao, ExternalModuleDao externalModuleDao) {
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
            PreparedStatement pst = connection.prepareStatement(INSERT_DOCUMENT, new String[]{"id"});
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

    private final RowMapper<Document> stubMapper = (resultSet, i) -> {
        Document doc = new Document();
        doc.setId(resultSet.getInt("id"));

        DocumentMetaData meta = new DocumentMetaData();
        meta.setAuthor(resultSet.getString("author"));
        meta.setTitle(resultSet.getString("title"));
        meta.setSource(resultSet.getString("source"));
        meta.setModule(externalModuleDao.get(resultSet.getString("module_id")));
        meta.setCrawlTime(getCalendarFromTime(resultSet.getDate("crawl_time")));
        meta.setCreationTime(getCalendarFromTime(resultSet.getDate("creation_time")));

        doc.setMetaData(meta);
        return doc;
    };

    private final RowMapper<Document> documentMapper = (resultSet, i) -> {
        Document doc = stubMapper.mapRow(resultSet, i);
        doc.setSentences(sentenceDao.findForDocument(doc));
        return doc;
    };
}
