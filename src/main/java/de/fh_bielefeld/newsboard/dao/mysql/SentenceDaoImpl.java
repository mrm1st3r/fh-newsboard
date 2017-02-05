package de.fh_bielefeld.newsboard.dao.mysql;

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
public class SentenceDaoImpl implements SentenceDao {

    private static final String GET_SENTENCE_WITH_ID = "SELECT * FROM sentence WHERE id = ?";
    private static final String GET_ALL_SENTENCES_IN_DOCUMENT = "SELECT * FROM sentence WHERE document_id = ?";
    private static final String UPDATE_SENTENCE = "UPDATE sentence SET number = ?, text = ? WHERE id = ?";
    private static final String INSERT_SENTENCE = "INSERT INTO sentence(number, text, document_id) VALUES (?, ?, ?)";

    private JdbcTemplate jdbcTemplate;
    private ClassificationDaoImpl classificationDao;

    @Autowired
    public SentenceDaoImpl(JdbcTemplate jdbcTemplate, ClassificationDaoImpl classificationDao) {
        this.jdbcTemplate = jdbcTemplate;
        this.classificationDao = classificationDao;
    }

    @Override
    public Sentence getSentenceWithId(int id) {
        return jdbcTemplate.query(GET_SENTENCE_WITH_ID, new RowMapperResultSetExtractor<>(sentenceRowMapper), id);
    }

    @Override
    public List<Sentence> getAllSentencesInDocument(Document document) {
        return jdbcTemplate.query(GET_ALL_SENTENCES_IN_DOCUMENT, sentenceRowMapper, document.getId());
    }

    @Override
    public int updateSentenceWithoutDocument(Sentence sentence) {
        return jdbcTemplate.update(UPDATE_SENTENCE, sentence.getNumber(), sentence.getText(), sentence.getId());
    }

    @Override
    public int insertSentence(Sentence sentence, Document document) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        int numRows = jdbcTemplate.update(connection -> {
            PreparedStatement pst = connection.prepareStatement(INSERT_SENTENCE, new String[]{"id"});
            pst.setInt(1, sentence.getNumber());
            pst.setString(2, sentence.getText());
            pst.setInt(3, document.getId());
            return pst;
        }, keyHolder);
        sentence.setId(keyHolder.getKey().intValue());
        return numRows;
    }

    private final RowMapper<Sentence> sentenceRowMapper = (resultSet, i) -> {
        Sentence sentence = new Sentence();
        sentence.setId(resultSet.getInt("id"));
        sentence.setNumber(resultSet.getInt("number"));
        sentence.setText(resultSet.getString("text"));
        sentence.setClassifications( classificationDao.getAllClassificationsForSentence(sentence));
        return sentence;
    };
}
