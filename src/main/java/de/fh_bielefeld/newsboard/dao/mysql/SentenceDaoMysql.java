package de.fh_bielefeld.newsboard.dao.mysql;

import de.fh_bielefeld.newsboard.dao.ClassificationDao;
import de.fh_bielefeld.newsboard.dao.SentenceDao;
import de.fh_bielefeld.newsboard.model.Document;
import de.fh_bielefeld.newsboard.model.Sentence;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import java.sql.PreparedStatement;
import java.util.List;

/**
 * MySQL implementation for Sentence DAO.
 */
@Component
public class SentenceDaoMysql implements SentenceDao {

    private static final String GET_ALL_SENTENCES_IN_DOCUMENT = "SELECT * FROM sentence WHERE document_id = ?";
    private static final String INSERT_SENTENCE = "INSERT INTO sentence(document_seq, content, document_id) VALUES (?, ?, ?)";

    private JdbcTemplate jdbcTemplate;
    private ClassificationDao classificationDao;

    @Autowired
    public SentenceDaoMysql(JdbcTemplate jdbcTemplate, ClassificationDao classificationDao) {
        this.jdbcTemplate = jdbcTemplate;
        this.classificationDao = classificationDao;
    }

    @Override
    public List<Sentence> findForDocument(Document document) {
        return jdbcTemplate.query(GET_ALL_SENTENCES_IN_DOCUMENT, sentenceRowMapper, document.getId());
    }

    @Override
    public int create(Sentence sentence, Document document) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        int numRows = jdbcTemplate.update(connection -> {
            PreparedStatement pst = connection.prepareStatement(INSERT_SENTENCE, new String[]{"sentence_id"});
            pst.setInt(1, sentence.getNumber());
            pst.setString(2, sentence.getText());
            pst.setInt(3, document.getId());
            return pst;
        }, keyHolder);
        sentence.setId(keyHolder.getKey().intValue());
        return numRows;
    }

    private final RowMapper<Sentence> sentenceRowMapper = (resultSet, i) -> {
        Sentence sentence = new Sentence(
                resultSet.getInt("sentence_id"),
                resultSet.getInt("document_seq"),
                resultSet.getString("content"));
        sentence.addClassifications(classificationDao.findForSentence(sentence));
        return sentence;
    };
}
