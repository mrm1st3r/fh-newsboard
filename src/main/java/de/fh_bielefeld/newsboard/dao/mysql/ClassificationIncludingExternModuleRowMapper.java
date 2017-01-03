package de.fh_bielefeld.newsboard.dao.mysql;

import de.fh_bielefeld.newsboard.model.Classification;
import de.fh_bielefeld.newsboard.model.Document;
import de.fh_bielefeld.newsboard.model.ExternModule;
import de.fh_bielefeld.newsboard.model.Sentence;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by felixmeyer on 02.01.17.
 */
public class ClassificationIncludingExternModuleRowMapper implements RowMapper<Classification> {

    private Object selectingParameterObject;

    public ClassificationIncludingExternModuleRowMapper(Object selectingParameterObject) {
        if (selectingParameterObject instanceof Sentence || selectingParameterObject instanceof Document) {
            throw new IllegalArgumentException("Selecting parameter object must be of type Sentence or Document.");
        } else {
            this.selectingParameterObject = selectingParameterObject;
        }
    }

    @Override
    public Classification mapRow(ResultSet resultSet, int i) throws SQLException {
        ExternModule module;
        String moduleId = resultSet.getString("c.module_id");
        if (moduleId == null) {
            module = null;
        } else {
            module = new ExternModule();
            module.setId(moduleId);
            module.setAuthor(resultSet.getString("em.author"));
            module.setDescription(resultSet.getString("em.description"));
            module.setName(resultSet.getString("em.name"));
        }

        Document document;
        if (selectingParameterObject instanceof Document) {
            document = (Document) selectingParameterObject;
        } else {
            document = new Document();
            document.setId(resultSet.getInt("c.document_id"));
        }

        Sentence sentence;
        if (selectingParameterObject instanceof Sentence) {
            sentence = (Sentence) selectingParameterObject;
        } else {
            sentence = new Sentence();
            sentence.setId(resultSet.getInt("c.sent_id"));
        }

        Classification classification = new Classification();
        classification.setDocument(document);
        classification.setSentence(sentence);
        classification.setExternModule(module);
        classification.setValue(resultSet.getDouble("c.value"));
        classification.setConfidence(resultSet.getDouble("c.confidence"));

        return classification;
    }
}
