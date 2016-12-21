package de.fh_bielefeld.newsboard.dao;

import de.fh_bielefeld.newsboard.model.Classification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by felixmeyer on 17.12.16.
 */
@Component
public class ClassificationDaoImpl implements ClassificationDao {
    private static final String GET_CLASSIFICATION_QUERY =
            "SELECT sent_id, document_id, module_id, value, confidence " +
                    "FROM classification " +
                    "WHERE sent_id = ? AND document_id = ? AND module_id = ?";
    private static final String GET_CLASSIFICATIONS_FOR_SENTENCE = "";


    @Autowired
    private SentenceDao sentenceDao;
    @Autowired
    private DocumentDao documentDao;
    @Autowired
    private ExternModuleDao externModuleDao;

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

    protected class ClassificationRowMapper implements RowMapper<Classification> {
        @Override
        public Classification mapRow(ResultSet resultSet, int i) throws SQLException {
            // TODO
            return null;
        }
    }
}
