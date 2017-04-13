package de.fhbielefeld.newsboard.dao.mysql;

import de.fhbielefeld.newsboard.dao.ClassificationDao;
import de.fhbielefeld.newsboard.model.Classification;
import de.fhbielefeld.newsboard.model.ModuleReference;
import de.fhbielefeld.newsboard.model.Sentence;
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
            "INSERT IGNORE INTO classification (confidence, result, sentence_id, module_id) VALUES (?, ?, ?, ?)";

    private final RowMapper<Classification> rowMapper = (resultSet, i) -> {
        double confidenceValue = resultSet.getDouble("confidence");
        OptionalDouble confidence;
        if (!resultSet.wasNull()) {
            confidence = OptionalDouble.of(confidenceValue);
        } else {
            confidence = OptionalDouble.empty();
        }
        return new Classification(
                resultSet.getInt("sentence_id"),
                new ModuleReference(resultSet.getString("module_id")),
                resultSet.getDouble("result"),
                confidence
        );
    };

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public ClassificationDaoMysql(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Classification> findForSentence(Sentence sentence) {
        return jdbcTemplate.query(GET_CLASSIFICATIONS_FOR_SENTENCE, rowMapper, (Object) sentence.getId());
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
}
