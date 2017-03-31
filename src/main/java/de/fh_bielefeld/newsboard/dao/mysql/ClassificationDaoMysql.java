package de.fh_bielefeld.newsboard.dao.mysql;

import de.fh_bielefeld.newsboard.dao.ClassificationDao;
import de.fh_bielefeld.newsboard.dao.ExternalModuleDao;
import de.fh_bielefeld.newsboard.model.Classification;
import de.fh_bielefeld.newsboard.model.ExternalModule;
import de.fh_bielefeld.newsboard.model.Sentence;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.OptionalDouble;

/**
 * Mysql implementation for Classification DAO.
 */
@Component
public class ClassificationDaoMysql implements ClassificationDao {

    private static final String GET_CLASSIFICATIONS_FOR_SENTENCE =
            "SELECT * FROM classification WHERE sentence_id = ?";
    private static final String INSERT_CLASSIFICATION =
            "INSERT INTO classification (confidence, result, sentence_id, module_id) VALUES (?, ?, ?, ?)";

    private JdbcTemplate jdbcTemplate;
    private ExternalModuleDao externalModuleDao;

    @Autowired
    public ClassificationDaoMysql(JdbcTemplate jdbcTemplate, ExternalModuleDao externalModuleDao) {
        this.jdbcTemplate = jdbcTemplate;
        this.externalModuleDao = externalModuleDao;
    }

    @Override
    public List<Classification> findForSentence(Sentence sentence) {
        List<Classification> classifications = jdbcTemplate.query(GET_CLASSIFICATIONS_FOR_SENTENCE, rowMapper, (Object) sentence.getId());

        for (Classification classification : classifications) {
            if (classification.getExternalModule() != null) {
                ExternalModule m = externalModuleDao.get(classification.getExternalModule().getId());
                classification.setExternalModule(m);
            } else {
                classification.setExternalModule(null);
            }
        }
        return classifications;
    }

    @Override
    public int create(Classification classification) {
        Double confidence = null;
        OptionalDouble optionalConfidence = classification.getConfidence();
        if (optionalConfidence.isPresent()) {
            confidence = optionalConfidence.getAsDouble();
        }
        return jdbcTemplate.update(INSERT_CLASSIFICATION, confidence,
                classification.getValue(),
                classification.getSentenceId(),
                classification.getExternalModule().getId());
    }

    private final RowMapper<Classification> rowMapper = (resultSet, i) -> {
        Classification classification = new Classification();

        classification.setSentenceId(resultSet.getInt("sentence_id"));
        classification.setExternalModule(new ExternalModule(resultSet.getString("module_id")));
        classification.setValue(resultSet.getDouble("result"));
        double confidence = resultSet.getDouble("confidence");
        if (!resultSet.wasNull()) {
            classification.setConfidence(confidence);
        }
        return classification;
    };
}
