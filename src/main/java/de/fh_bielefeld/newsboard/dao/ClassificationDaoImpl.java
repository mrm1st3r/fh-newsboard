package de.fh_bielefeld.newsboard.dao;

import de.fh_bielefeld.newsboard.model.Classification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

/**
 * Created by felixmeyer on 17.12.16.
 */
public class ClassificationDaoImpl implements ClassificationDao {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public ClassificationDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Classification getClassification(int sentenceId, int documentId, String moduleId) {
        return null;
    }

    @Override
    public List<Classification> getAllClassificationsForSentence(int sentId) {
        return null;
    }

    @Override
    public List<Classification> getAllClassificationsForDocument(int docId) {
        return null;
    }

    @Override
    public List<Classification> getAllClassificationsFromModule(String moduleId) {
        return null;
    }

    @Override
    public int updateClassification(Classification classficiation) {
        return 0;
    }

    @Override
    public int insertClassification(Classification classification) {
        return 0;
    }
}
