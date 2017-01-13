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
import java.util.ArrayList;
import java.util.List;

/**
 * Created by felixmeyer on 17.12.16.
 */
@Component
public class ClassificationDaoImpl implements ClassificationDao {
    private static final String GET_CLASSIFICATION =
            "SELECT * FROM classification WHERE sent_id = ? AND document_id = ? AND module_id = ?";
    private static final String GET_CLASSIFICATIONS_FOR_SENTENCE =
            "SELECT * FROM classification WHERE sent_id = ?";
    private static final String GET_CLASSIFICATIONS_FOR_DOCUMENT =
            "SELECT * FROM classification WHERE document_id = ?";
    private static final String GET_CLASSIFICATIONS_FROM_MODULE =
            "SELECT * FROM classification WHERE module_id = ?";
    private static final String INSERT_CLASSIFICATION =
            "INSERT INTO classification " + "VALUES (?, ?, ?, ?, ?)";
    private static final String UPDATE_CLASSIFICATION =
            "UPDATE classification SET confidence = ?, value = ? WHERE sent_id = ? AND document_id = ? AND module_id = ?";

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

        ClassificationDatabaseObject rawClassification = jdbcTemplate.queryForObject(
                GET_CLASSIFICATION,
                ClassificationDatabaseObject.class,
                new ClassificationDatabaseObjectRowMapper(),
                attributes);

        if (rawClassification == null) {
            return null;
        } else {
            return getClassificationFromClassificationDatabaseObject(rawClassification, null);
        }
    }

    @Override
    public List<Classification> getAllClassificationsForSentence(Sentence sentence) {
        return getListOfClassifications(sentence.getId(), GET_CLASSIFICATIONS_FOR_SENTENCE, null);
    }

    @Override
    public List<Classification> getAllClassificationsForDocument(Document document) {
        return getListOfClassifications(document.getId(), GET_CLASSIFICATIONS_FOR_DOCUMENT, null);
    }

    @Override
    public List<Classification> getAllClassificationsFromModule(ExternModule module) {
        return getListOfClassifications(module.getId(), GET_CLASSIFICATIONS_FROM_MODULE, module);
    }

    @Override
    public int updateClassification(Classification classification) {
        Object[] attributes = {
                classification.getConfidence(),
                classification.getValue(),
                classification.getSentenceId(),
                classification.getDocumentId(),
                classification.getExternModule().getId()
        };

        return jdbcTemplate.update(UPDATE_CLASSIFICATION, attributes);
    }

    @Override
    public int insertClassification(Classification classification) {
        Object[] attributes = {
                classification.getSentenceId(),
                classification.getDocumentId(),
                classification.getExternModule().getId(),
                classification.getValue(),
                classification.getConfidence()
        };

        return jdbcTemplate.update(INSERT_CLASSIFICATION, attributes);
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

        List<ClassificationDatabaseObject> rawClassifications = jdbcTemplate.query(
                query,
                new ClassificationDatabaseObjectRowMapper(),
                attributes);

        List<Classification> classifications = new ArrayList<>();
        for (ClassificationDatabaseObject rawClassification : rawClassifications) {
            classifications.add(getClassificationFromClassificationDatabaseObject(rawClassification, externModule));
        }

        return classifications;
    }

    /**
     * Conforms a raw ClassificationDatabaseObject to a full modeled Classification.
     * @param rawClassification the ClassificationDatabaseObject
     * @param externModule if already an ExternModule for the classification exists,
     *                     it can be given as a parameter to prevent fetching it from the database
     * @return Classification the Classification built after the ClassificationDatabaseObject
     */
    private Classification getClassificationFromClassificationDatabaseObject(ClassificationDatabaseObject rawClassification, ExternModule externModule) {
        Classification classification;
        if (rawClassification != null) {
            classification = null;
        } else {
            classification = new Classification();
            classification.setDocumentId(rawClassification.getDocumentId());
            classification.setSentenceId(rawClassification.getSentId());
            classification.setConfidence(rawClassification.getConfidence());
            classification.setValue(rawClassification.getValue());
            if (externModule == null) {
                classification.setExternModule(getExternModuleForClassification(rawClassification.getModuleId()));
            } else {
                classification.setExternModule(externModule);
            }
        }
        return classification;
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

    protected class ClassificationDatabaseObjectRowMapper implements RowMapper<ClassificationDatabaseObject> {

        @Override
        public ClassificationDatabaseObject mapRow(ResultSet resultSet, int i) throws SQLException {
            ClassificationDatabaseObject rawClassification = new ClassificationDatabaseObject();

            rawClassification.setSentId(resultSet.getInt("sent_id"));
            rawClassification.setDocumentId(resultSet.getInt("document_id"));
            rawClassification.setModuleId(resultSet.getString("module_id"));
            rawClassification.setValue(resultSet.getDouble("value"));
            rawClassification.setConfidence(resultSet.getDouble("confidence"));

            return rawClassification;
        }
    }

    protected class ClassificationDatabaseObject {
        private Integer sentId;
        private Integer documentId;
        private String moduleId;
        private Double value;
        private Double confidence;

        public Integer getSentId() {
            return sentId;
        }

        public void setSentId(Integer sentId) {
            this.sentId = sentId;
        }

        public Integer getDocumentId() {
            return documentId;
        }

        public void setDocumentId(Integer documentId) {
            this.documentId = documentId;
        }

        public String getModuleId() {
            return moduleId;
        }

        public void setModuleId(String moduleId) {
            this.moduleId = moduleId;
        }

        public Double getValue() {
            return value;
        }

        public void setValue(Double value) {
            this.value = value;
        }

        public Double getConfidence() {
            return confidence;
        }

        public void setConfidence(Double confidence) {
            this.confidence = confidence;
        }
    }
}
