package de.fh_bielefeld.newsboard.dao.mysql;

import de.fh_bielefeld.newsboard.dao.ClassificationDao;
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
            "SELECT * FROM classification AS c LEFT JOIN extern_module AS em ON c.module_id = em.id WHERE sent_id = ?";
    private static final String GET_CLASSIFICATIONS_FOR_DOCUMENT =
            "SELECT * FROM classification AS c LEFT JOIN extern_module AS em ON c.module_id = em.id WHERE document_id = ?";
    private static final String GET_CLASSIFICATIONS_FROM_MODULE =
            "SELECT * FROM classification WHERE module_id = ?";
    private static final String INSERT_CLASSIFICATION =
            "INSERT INTO classification " + "VALUES (?, ?, ?, ?, ?)";
    private static final String UPDATE_CLASSIFICATION =
            "UPDATE classification SET confidence = ?, value = ? WHERE sent_id = ? AND document_id = ? AND module_id = ?";

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public ClassificationDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Classification getClassification(Sentence sentence, Document document, ExternModule module) {
        Object[] attributes = new Object[] {
                sentence.getId(),
                document.getId(),
                module.getId()
        };
        ClassificationDatabaseObject rawClassification = jdbcTemplate.queryForObject(
                GET_CLASSIFICATION,
                attributes,
                new ClassificationDatabaseObjectRowMapper());

        if (rawClassification == null) {
            return null;
        } else {
            Classification classification = new Classification(
                sentence,
                document,
                module,
                rawClassification.getValue(),
                rawClassification.getConfidence()
            );
            return classification;
        }
    }

    @Override
    public List<Classification> getAllClassificationsForSentence(Sentence sentence) {
        Object[] attributes = new Object[] {
                sentence.getId()
        };

        List<Classification> classifications = jdbcTemplate.query(
                GET_CLASSIFICATIONS_FOR_SENTENCE,
                attributes,
                new ClassificationIncludingExternModuleRowMapper(sentence));

        return classifications;
    }

    @Override
    public List<Classification> getAllClassificationsForDocument(Document document) {
        Object[] attributes = new Object[] {
                document.getId()
        };

        List<Classification> classifications = jdbcTemplate.query(
                GET_CLASSIFICATIONS_FOR_DOCUMENT,
                attributes,
                new ClassificationIncludingExternModuleRowMapper(document));

        return classifications;
    }

    @Override
    public List<Classification> getAllClassificationsFromModule(ExternModule module) {
        Object[] attributes = new Object[] {
                module.getId()
        };

        List<ClassificationDatabaseObject> rawClassifications = jdbcTemplate.query(
                GET_CLASSIFICATIONS_FROM_MODULE,
                attributes,
                new ClassificationDatabaseObjectRowMapper());

        List<Classification> classifications = new ArrayList<>();
        for (ClassificationDatabaseObject rawClassification : rawClassifications) {
            Classification classification = new Classification();
            Sentence dummySentence = new Sentence();
            Document dummyDocument = new Document();
            dummySentence.setId(rawClassification.getSentId());
            dummyDocument.setId(rawClassification.getDocumentId());
            classification.setSentence(dummySentence);
            classification.setDocument(dummyDocument);
            classification.setExternModule(module);
            classification.setConfidence(rawClassification.getConfidence());
            classification.setValue(rawClassification.getValue());
        }

        return classifications;
    }

    @Override
    public int updateClassification(Classification classification) {
        Sentence sent = classification.getSentence();
        Document doc = classification.getDocument();
        ExternModule module = classification.getExternModule();

        Object[] attributes = new Object[5];
        attributes[0] = sent == null ? null : sent.getId();
        attributes[1] = doc == null ? null : doc.getId();
        attributes[2] = module == null ? null : module.getId();
        attributes[3] = classification.getValue();
        attributes[4] = classification.getConfidence();

        return jdbcTemplate.update(UPDATE_CLASSIFICATION, attributes);
    }

    @Override
    public int insertClassification(Classification classification) {
        Sentence sent = classification.getSentence();
        Document doc = classification.getDocument();
        ExternModule module = classification.getExternModule();

        Object[] attributes = new Object[5];
        attributes[0] = classification.getValue();
        attributes[1] = classification.getConfidence();
        attributes[2] = sent == null ? null : sent.getId();
        attributes[3] = doc == null ? null : doc.getId();
        attributes[4] = module == null ? null : module.getId();

        return jdbcTemplate.update(INSERT_CLASSIFICATION, attributes);
    }

    protected class ClassificationRowMapper implements RowMapper<Classification> {
        @Override
        public Classification mapRow(ResultSet resultSet, int i) throws SQLException {
            return null;
        }
    }
}
