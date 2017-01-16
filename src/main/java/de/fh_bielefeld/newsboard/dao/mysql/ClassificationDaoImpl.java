package de.fh_bielefeld.newsboard.dao.mysql;

import de.fh_bielefeld.newsboard.dao.ClassificationDao;
import de.fh_bielefeld.newsboard.dao.ExternModuleDao;
import de.fh_bielefeld.newsboard.model.Classification;
import de.fh_bielefeld.newsboard.model.Document;
import de.fh_bielefeld.newsboard.model.ExternModule;
import de.fh_bielefeld.newsboard.model.Sentence;
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
    private static final String GET_CLASSIFICATION =
            "SELECT * FROM classification WHERE sent_id = ? AND module_id = ?";
    private static final String GET_CLASSIFICATIONS_FOR_SENTENCE =
            "SELECT * FROM classification WHERE sent_id = ?";
    private static final String GET_CLASSIFICATIONS_FROM_MODULE =
            "SELECT * FROM classification WHERE module_id = ?";
    private static final String INSERT_CLASSIFICATION =
            "INSERT INTO classification (sent_id, value, confidence) VALUES (?, ?, ?)";
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
    public Classification getClassification(Sentence sentence, Document document, ExternModule module) {
        Object[] attributes = {
                sentence.getId(),
                document.getId(),
                module.getId(),
        };

        Classification classification = jdbcTemplate.queryForObject(
                GET_CLASSIFICATION,
                new ClassificationRowMapper(),
                attributes);

        if (classification == null) {
            return null;
        } else {
            classification.setExternModule(module);
            classification.setSentenceId(sentence.getId());
            return classification;
        }
    }

    @Override
    public List<Classification> getAllClassificationsForSentence(Sentence sentence) {
        return getListOfClassifications(sentence.getId(), GET_CLASSIFICATIONS_FOR_SENTENCE, null);
    }

    @Override
    public List<Classification> getAllClassificationsFromModule(ExternModule module) {
        return getListOfClassifications(module.getId(), GET_CLASSIFICATIONS_FROM_MODULE, module);
    }

    @Override
    public int updateClassification(Classification classification) {
        return jdbcTemplate.update(UPDATE_CLASSIFICATION, getAttributesForInsertOrUpdate(classification, false));
    }

    @Override
    public int insertClassification(Classification classification) {
        return jdbcTemplate.update(INSERT_CLASSIFICATION, getAttributesForInsertOrUpdate(classification, true));
    }

    private Object[] getAttributesForInsertOrUpdate(Classification classification, boolean isInsert) {
        String moduleId = classification.getExternModule() == null ? null : classification.getExternModule().getId();
        Integer sentenceId = classification.getSentenceId().getAsInt();
        Double confidence = classification.getConfidence().getAsDouble();

        Object[] attributes;
        if (isInsert) {
            attributes = new Object[] {
                    sentenceId,
                    moduleId,
                    classification.getValue(),
                    confidence
            };
        } else {
            attributes = new Object[] {
                    classification.getValue(),
                    confidence,
                    sentenceId,
                    moduleId
            };
        }
        return attributes;
    }

    /**
     * Fetches a list containing all relevant Classifications from the Database.
     * @param attribute the attribute which will be used to select the rows in the database
     * @param query the executed query
     * @param externModule when giving an explicit ExternModule, no try to fetch one from database will be executed
     * @return List containing the classifications
     */
    private List<Classification> getListOfClassifications(Object attribute, String query, ExternModule externModule) {
        Object[] attributes = {
                attribute
        };

        List<Classification> classifications = jdbcTemplate.query(
                query,
                new ClassificationRowMapper(),
                attributes);

        for (Classification classification : classifications) {
            if (externModule == null && classification.getExternModule() != null) {
                ExternModule m = getExternModuleForClassification(classification.getExternModule().getId());
                classification.setExternModule(m);
            } else {
                classification.setExternModule(externModule);
            }
            classifications.add(classification);
        }

        return classifications;
    }

    /**
     * Fetches an ExternModule per Dao from the database.
     * @param moduleId the ExternModules id
     * @return the ExternModule
     */
    private ExternModule getExternModuleForClassification(String moduleId) {
        ExternModule externModule;
        if (moduleId == null) {
            externModule = null;
        } else {
            externModule = externModuleDao.getExternModuleWithId(moduleId);
        }
        return externModule;
    }

    protected class ClassificationRowMapper implements RowMapper<Classification> {

        @Override
        public Classification mapRow(ResultSet resultSet, int i) throws SQLException {
            Classification classification = new Classification();

            classification.setSentenceId(resultSet.getInt("sent_id"));
            classification.setExternModule(new ExternModule(resultSet.getString("module_id")));
            classification.setValue(resultSet.getDouble("value"));
            classification.setConfidence(resultSet.getDouble("confidence"));

            return classification;
        }
    }
}
