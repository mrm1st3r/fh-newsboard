package de.fh_bielefeld.newsboard.dao.mysql;

import de.fh_bielefeld.newsboard.dao.ClassificationDao;
import de.fh_bielefeld.newsboard.dao.DocumentDao;
import de.fh_bielefeld.newsboard.dao.ExternModuleDao;
import de.fh_bielefeld.newsboard.dao.SentenceDao;
import de.fh_bielefeld.newsboard.model.Sentence;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by felixmeyer on 17.12.16.
 */
@Component
public class SentenceDaoImpl implements SentenceDao {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public SentenceDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Sentence getSentenceWithId(int id) {
        return null;
    }

    @Override
    public List<Sentence> getAllSentencesInDocument(int documentId) {
        return null;
    }

    @Override
    public int updateSentence(Sentence sentence) {
        return 0;
    }

    @Override
    public int insertSentence(Sentence sentence) {
        return 0;
    }
}
