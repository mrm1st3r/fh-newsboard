package de.fhbielefeld.newsboard.dao.mysql;

import de.fhbielefeld.newsboard.model.document.Document;
import de.fhbielefeld.newsboard.model.document.DocumentId;
import de.fhbielefeld.newsboard.model.document.Sentence;
import de.fhbielefeld.newsboard.model.document.SentenceDao;
import io.vavr.collection.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import java.sql.PreparedStatement;

/**
 * MySQL implementation for Sentence DAO.
 */
@Component
public class SentenceDaoMysql implements SentenceDao {

    private static final String GET_ALL_SENTENCES_IN_DOCUMENT = "SELECT * FROM sentence WHERE document_id = ?";
    private static final String INSERT_SENTENCE = "INSERT INTO sentence(document_seq, content, document_id) VALUES (?, ?, ?)";

    private final JdbcTemplate jdbcTemplate;

    private final RowMapper<Sentence> sentenceRowMapper = (resultSet, i) -> new Sentence(
            resultSet.getInt("sentence_id"),
            resultSet.getInt("document_seq"),
            resultSet.getString("content"));

    @Autowired
    public SentenceDaoMysql(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Sentence> findForDocument(DocumentId id) {
        return List.ofAll(jdbcTemplate.query(GET_ALL_SENTENCES_IN_DOCUMENT, sentenceRowMapper, id.raw()));
    }

    @Override
    public int create(Sentence sentence, Document document) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        int numRows = jdbcTemplate.update(connection -> {
            PreparedStatement pst = connection.prepareStatement(INSERT_SENTENCE, new String[]{"sentence_id"});
            pst.setInt(1, sentence.getNumber());
            pst.setString(2, sentence.getText());
            pst.setInt(3, document.getId().raw());
            return pst;
        }, keyHolder);
        sentence.setId(keyHolder.getKey().intValue());
        return numRows;
    }
}
