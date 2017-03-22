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

    private static final String GET_CLASSIFICATION =
            "SELECT * FROM classification WHERE sent_id = ? AND module_id = ?";
    private static final String GET_CLASSIFICATIONS_FOR_SENTENCE =
            "SELECT * FROM classification WHERE sent_id = ?";
    private static final String GET_CLASSIFICATIONS_FROM_MODULE =
            "SELECT * FROM classification WHERE module_id = ?";
    private static final String INSERT_CLASSIFICATION =
            "INSERT INTO classification (confidence, value, sent_id, module_id) VALUES (?, ?, ?, ?)";
    private static final String UPDATE_CLASSIFICATION =
            "UPDATE classification SET confidence = ?, value = ? WHERE sent_id = ? AND module_id = ?";

    private JdbcTemplate jdbcTemplate;
    private ExternalModuleDao externalModuleDao;

    @Autowired
    public ClassificationDaoMysql(JdbcTemplate jdbcTemplate, ExternalModuleDao externalModuleDao) {
        this.jdbcTemplate = jdbcTemplate;
        this.externalModuleDao = externalModuleDao;
    }

    @Override
    public Classification get(Sentence sentence, ExternalModule module) {
        Classification classification = jdbcTemplate.query(GET_CLASSIFICATION,
                new RowMapperResultSetExtractor<>(rowMapper), sentence.getId(), module.getId());
        if (classification == null) {
            return null;
        }
        classification.setExternalModule(module);
        classification.setSentenceId(sentence.getId());
        return classification;
    }

    @Override
    public List<Classification> findForSentence(Sentence sentence) {
        return getListOfClassifications(sentence.getId(), GET_CLASSIFICATIONS_FOR_SENTENCE, null);
    }

    @Override
    public List<Classification> findForModule(ExternalModule module) {
        return getListOfClassifications(module.getId(), GET_CLASSIFICATIONS_FROM_MODULE, module);
    }

    @Override
    public int update(Classification classification) {
        return jdbcTemplate.update(UPDATE_CLASSIFICATION, getAttributesForInsertOrUpdate(classification));
    }

    @Override
    public int create(Classification classification) {
        return jdbcTemplate.update(INSERT_CLASSIFICATION, getAttributesForInsertOrUpdate(classification));
    }

    private Object[] getAttributesForInsertOrUpdate(Classification classification) {
        Double confidence = null;
        OptionalDouble optionalConfidence = classification.getConfidence();
        if (optionalConfidence.isPresent()) {
            confidence = optionalConfidence.getAsDouble();
        }
        return new Object[] {
                confidence,
                classification.getValue(),
                classification.getSentenceId(),
                classification.getExternalModule().getId()
        };
    }

    private List<Classification> getListOfClassifications(Object attribute, String query, ExternalModule externalModule) {
        List<Classification> classifications = jdbcTemplate.query(query, rowMapper, attribute);

        for (Classification classification : classifications) {
            if (externalModule == null && classification.getExternalModule() != null) {
                ExternalModule m = externalModuleDao.get(classification.getExternalModule().getId());
                classification.setExternalModule(m);
            } else {
                classification.setExternalModule(externalModule);
            }
        }
        return classifications;
    }

    private final RowMapper<Classification> rowMapper = (resultSet, i) -> {
        Classification classification = new Classification();

        classification.setSentenceId(resultSet.getInt("sent_id"));
        classification.setExternalModule(new ExternalModule(resultSet.getString("module_id")));
        classification.setValue(resultSet.getDouble("value"));
        double confidence = resultSet.getDouble("confidence");
        if (!resultSet.wasNull()) {
            classification.setConfidence(confidence);
        }
        return classification;
    };
}
