package de.fhbielefeld.newsboard.dao.mysql;

import de.fhbielefeld.newsboard.model.document.*;
import de.fhbielefeld.newsboard.model.module.ModuleId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Mysql implementation for Classification DAO.
 */
@Component
public class ClassificationDaoMysql implements ClassificationDao {

    private static final String GET_CLASSIFICATIONS_FOR_SENTENCE =
            "SELECT * FROM classification c " +
                    "LEFT JOIN classification_value v ON c.classification_id = v.classification_id " +
                    "WHERE c.document_id = ?";
    private static final String INSERT_CLASSIFICATION =
            "INSERT INTO classification (document_id, module_id, created) VALUES (?, ?, ?)";
    private static final String INSERT_CLASSIFICATION_VALUE =
            "INSERT INTO classification_value (classification_id, order_seq, classification, confidence) VALUES (?, ?, ?, ?)";

    private static final String CLASSIFICATION_ID = "classification_id";

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public ClassificationDaoMysql(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<DocumentClassification> findForDocument(Document document) {
        return jdbcTemplate.query(GET_CLASSIFICATIONS_FOR_SENTENCE, new ClassificationResultExtractor(), document.getId().raw());
    }

    @Override
    public int create(DocumentClassification classification) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        int updatedRows = jdbcTemplate.update(connection -> {
            PreparedStatement pst = connection.prepareStatement(INSERT_CLASSIFICATION, new String[]{CLASSIFICATION_ID});
            pst.setInt(1, classification.getDocumentId().raw());
            pst.setString(2, classification.getModule().raw());
            pst.setDate(3, Date.valueOf(LocalDate.now()));
            return pst;
        }, keyHolder);

        int sequence = 1;
        for (ClassificationValue v : classification.getValues()) {
            updatedRows += jdbcTemplate.update(INSERT_CLASSIFICATION_VALUE,
                    keyHolder.getKey().intValue(),
                    sequence++,
                    v.getValue(),
                    v.getConfidence());
        }
        return updatedRows;
    }

    private class ClassificationResultExtractor implements ResultSetExtractor<List<DocumentClassification>> {

        @Override
        public List<DocumentClassification> extractData(ResultSet resultSet) throws SQLException {
            List<DocumentClassification> classifications = new ArrayList<>();
            while (resultSet.next()) {
                io.vavr.collection.List<ClassificationValue> values = io.vavr.collection.List.of();
                int classificationId = resultSet.getInt(CLASSIFICATION_ID);
                int documentId = resultSet.getInt("document_id");
                String moduleId = resultSet.getString("module_id");
                while (!resultSet.isAfterLast() && classificationId == resultSet.getInt(CLASSIFICATION_ID)) {
                    values = values.prepend(ClassificationValue.of(
                            resultSet.getDouble("classification"),
                            resultSet.getDouble("confidence")
                    ));
                    resultSet.next();
                }
                classifications.add(new DocumentClassification(
                        new DocumentId(documentId), new ClassificationId(classificationId),
                        new ModuleId(moduleId),
                        values
                ));
            }
            return classifications;
        }
    }
}
