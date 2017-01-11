package de.fh_bielefeld.newsboard.dao.mysql;

import de.fh_bielefeld.newsboard.dao.ClassificationDao;
import de.fh_bielefeld.newsboard.dao.DocumentDao;
import de.fh_bielefeld.newsboard.dao.ExternModuleDao;
import de.fh_bielefeld.newsboard.dao.SentenceDao;
import de.fh_bielefeld.newsboard.model.Classification;
import de.fh_bielefeld.newsboard.model.Document;
import de.fh_bielefeld.newsboard.model.ExternModule;
import de.fh_bielefeld.newsboard.model.Sentence;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by felixmeyer on 17.12.16.
 */
@Component
public class SentenceDaoImpl implements SentenceDao {
    private static final String GET_SENTENCE_WITH_ID =
            "SELECT * FROM SENTENCE WHERE id = ?";
    private static final String GET_ALL_SENTENCES_IN_DOCUMENT =
            "SELECT * FROM SENTENCE WHERE document_id = ?";
    private static final String UPDATE_SENTENCE_WITHOUT_DOCUMENT_ID =
            "UPDATE sentence SET number = ?, text = ? WHERE id = ?";
    private static final String UPDATE_SENTENCE_WITH_DOCUMENT_ID =
            "UPDATE sentence SET number = ?, text = ?, document_id = ? WHERE id = ?";
    private static final String INSERT_SENTENCE =
            "INSERT INTO sentence VALUES (?, ?, ?)";

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
                SentenceDatabaseObject.class,
                attributes,
                new SentenceDatabaseObjectRowMapper());

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
    public int updateSentenceWithDocument(Sentence sentence, Document document) {
        Object[] attributes = {
                sentence.getNumber(),
                sentence.getText(),
                document.getId(),
                sentence.getId()
        };

        return jdbcTemplate.update(UPDATE_SENTENCE_WITH_DOCUMENT_ID, attributes);
    }

    @Override
    public int insertSentence(Sentence sentence, Document document) {
        Object[] attributes = {
                sentence.getNumber(),
                sentence.getText(),
                document.getId()
        };

        return jdbcTemplate.update(INSERT_SENTENCE, attributes);
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
}