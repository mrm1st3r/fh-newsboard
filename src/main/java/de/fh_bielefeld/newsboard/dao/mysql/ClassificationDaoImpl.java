package de.fh_bielefeld.newsboard.dao.mysql;

import de.fh_bielefeld.newsboard.dao.ClassificationDao;
import de.fh_bielefeld.newsboard.dao.ExternModuleDao;
import de.fh_bielefeld.newsboard.model.Classification;
import de.fh_bielefeld.newsboard.model.ExternalModule;
import de.fh_bielefeld.newsboard.model.Sentence;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.OptionalDouble;

/**
 * Mysql implementation for Classification DAO.
 */
@Component
public class ClassificationDaoImpl implements ClassificationDao {
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
    private ExternModuleDao externModuleDao;

    @Autowired
    public ClassificationDaoImpl(JdbcTemplate jdbcTemplate, ExternModuleDao externModuleDao) {
        this.jdbcTemplate = jdbcTemplate;
        this.externModuleDao = externModuleDao;
    }

    @Override
    public Classification getClassification(Sentence sentence, ExternalModule module) {
        Object[] attributes = {
                sentence.getId(),
                module.getId(),
        };

        Classification classification = jdbcTemplate.queryForObject(
                GET_CLASSIFICATION,
                new ClassificationRowMapper(),
                attributes);

        if (classification == null) {
            return null;
        } else {
            classification.setExternalModule(module);
            classification.setSentenceId(sentence.getId());
            return classification;
        }
    }

    @Override
    public List<Classification> getAllClassificationsForSentence(Sentence sentence) {
        return getListOfClassifications(sentence.getId(), GET_CLASSIFICATIONS_FOR_SENTENCE, null);
    }

    @Override
    public List<Classification> getAllClassificationsFromModule(ExternalModule module) {
        return getListOfClassifications(module.getId(), GET_CLASSIFICATIONS_FROM_MODULE, module);
    }

    @Override
    public int updateClassification(Classification classification) {
        return jdbcTemplate.update(UPDATE_CLASSIFICATION, getAttributesForInsertOrUpdate(classification));
    }

    @Override
    public int insertClassification(Classification classification) {
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

    /**
     * Fetches a list containing all relevant Classifications from the Database.
     * @param attribute the attribute which will be used to select the rows in the database
     * @param query the executed query
     * @param externalModule when giving an explicit ExternalModule, no try to fetch one from database will be executed
     * @return List containing the classifications
     */
    private List<Classification> getListOfClassifications(Object attribute, String query, ExternalModule externalModule) {
        Object[] attributes = {
                attribute
        };

        List<Classification> classifications = jdbcTemplate.query(
                query,
                new ClassificationRowMapper(),
                attributes);

        for (Classification classification : classifications) {
            if (externalModule == null && classification.getExternalModule() != null) {
                ExternalModule m = getExternModuleForClassification(classification.getExternalModule().getId());
                classification.setExternalModule(m);
            } else {
                classification.setExternalModule(externalModule);
            }
        }

        return classifications;
    }

    /**
     * Fetches an ExternalModule per Dao from the database.
     * @param moduleId the ExternModules id
     * @return the ExternalModule
     */
    private ExternalModule getExternModuleForClassification(String moduleId) {
        ExternalModule externalModule;
        if (moduleId == null) {
            externalModule = null;
        } else {
            externalModule = externModuleDao.getExternModuleWithId(moduleId);
        }
        return externalModule;
    }

    private class ClassificationRowMapper implements RowMapper<Classification> {

        @Override
        public Classification mapRow(ResultSet resultSet, int i) throws SQLException {
            Classification classification = new Classification();

            classification.setSentenceId(resultSet.getInt("sent_id"));
            classification.setExternalModule(new ExternalModule(resultSet.getString("module_id")));
            classification.setValue(resultSet.getDouble("value"));
            double confidence = resultSet.getDouble("confidence");
            if (!resultSet.wasNull()) {
                classification.setConfidence(confidence);
            }
            return classification;
        }
    }
}
