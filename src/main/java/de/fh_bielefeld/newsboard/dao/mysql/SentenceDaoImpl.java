package de.fh_bielefeld.newsboard.dao.mysql;

import de.fh_bielefeld.newsboard.dao.SentenceDao;
import de.fh_bielefeld.newsboard.model.Classification;
import de.fh_bielefeld.newsboard.model.Document;
import de.fh_bielefeld.newsboard.model.Sentence;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import java.lang.reflect.GenericArrayType;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by felixmeyer on 17.12.16.
 */
@Component
public class SentenceDaoImpl implements SentenceDao {
    private static final String GET_SENTENCE_WITH_ID =
            "SELECT * FROM sentence WHERE id = ?";
    private static final String GET_ALL_SENTENCES_IN_DOCUMENT =
            "SELECT * FROM sentence WHERE document_id = ?";
    private static final String UPDATE_SENTENCE_WITHOUT_DOCUMENT_ID =
            "UPDATE sentence SET number = ?, text = ? WHERE id = ?";
    private static final String INSERT_SENTENCE =
            "INSERT INTO sentence(number, text, document_id) VALUES (?, ?, ?)";

    private JdbcTemplate jdbcTemplate;
    private ClassificationDaoImpl classificationDao;

    @Autowired
    public SentenceDaoImpl(JdbcTemplate jdbcTemplate, ClassificationDaoImpl classificationDao) {
        this.jdbcTemplate = jdbcTemplate;
        this.classificationDao = classificationDao;
    }

    @Override
    public Sentence getSentenceWithId(int id) {
        Object[] attributes = new Object[] {
                id
        };

        SentenceDatabaseObject rawSentence = jdbcTemplate.queryForObject(
                GET_SENTENCE_WITH_ID,
                new SentenceDatabaseObjectRowMapper(),
                attributes);

        return getSentenceFromSentenceDatabaseObject(rawSentence);
    }

    @Override
    public List<Sentence> getAllSentencesInDocument(Document document) {
        Object[] attributes = {
                document.getId()
        };

        List<SentenceDatabaseObject> rawSentences = jdbcTemplate.query(
                GET_ALL_SENTENCES_IN_DOCUMENT,
                new SentenceDatabaseObjectRowMapper(),
                attributes);

        List<Sentence> sentences = new ArrayList<>();
        for (SentenceDatabaseObject rawSentence : rawSentences) {
            Sentence sentence = getSentenceFromSentenceDatabaseObject(rawSentence);
            sentences.add(sentence);
        }

        return sentences;
    }

    @Override
    public int updateSentenceWithoutDocument(Sentence sentence) {
        Object[] attributes = {
                sentence.getNumber(),
                sentence.getText(),
                sentence.getId()
        };

        return jdbcTemplate.update(UPDATE_SENTENCE_WITHOUT_DOCUMENT_ID, attributes);
    }

    @Override
    public int insertSentence(Sentence sentence, Document document) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        int numRows = jdbcTemplate.update(new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                PreparedStatement pst =
                        connection.prepareStatement(INSERT_SENTENCE, new String[]{"id"});
                pst.setInt(1, sentence.getNumber());
                pst.setString(2, sentence.getText());
                pst.setInt(3, document.getId());
                return pst;
            }
        }, keyHolder);
        sentence.setId(keyHolder.getKey().intValue());
        return numRows;
    }

    /**
     * Fetches all classifications from the database and adds them to the sentence using the ClassificationDaoImpl.
     * @param sentence the Sentence of which the classifications should be fetched
     */
    private void addAllClassificationsToSentence(Sentence sentence) {
        for (Classification c : classificationDao.getAllClassificationsForSentence(sentence)) {
            sentence.addClassification(c);
        }
    }

    /**
     * Builds a full modelled Sentence out of a SentenceDatabaseObject.
     * @param rawSentence the SentenceDatabaseObject which contains the information about the sentence.
     * @return Sentence the Sentence
     */
    private Sentence getSentenceFromSentenceDatabaseObject(SentenceDatabaseObject rawSentence) {
        Sentence sentence = rawSentence == null ? null : new Sentence();
        if (sentence != null) {
            sentence.setId(rawSentence.getId());
            sentence.setNumber(rawSentence.getNumber());
            sentence.setText(rawSentence.getText());
            addAllClassificationsToSentence(sentence);
        }
        return sentence;
    }

    protected class SentenceDatabaseObjectRowMapper implements RowMapper<SentenceDatabaseObject> {
        @Override
        public SentenceDatabaseObject mapRow(ResultSet resultSet, int i) throws SQLException {
            SentenceDatabaseObject sentence = new SentenceDatabaseObject();
            sentence.setId(resultSet.getInt("id"));
            sentence.setNumber(resultSet.getInt("number"));
            sentence.setText(resultSet.getString("text"));
            sentence.setDocumentId(resultSet.getInt("document_id"));
            return sentence;
        }
    }

    protected class SentenceDatabaseObject {
        private Integer id;
        private Integer number;
        private String text;
        private Integer documentId;

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public Integer getNumber() {
            return number;
        }

        public void setNumber(Integer number) {
            this.number = number;
        }

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }

        public Integer getDocumentId() {
            return documentId;
        }

        public void setDocumentId(Integer documentId) {
            this.documentId = documentId;
        }
    }
}
