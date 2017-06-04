package de.fhbielefeld.newsboard.dao.mysql;

import de.fhbielefeld.newsboard.model.ExternalDocument;
import de.fhbielefeld.newsboard.model.ExternalDocumentDao;
import de.fhbielefeld.newsboard.model.module.ModuleId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import java.sql.PreparedStatement;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * MySQL Implementation for ExternalDocument DAO.
 */
@Component
public class ExternalDocumentDaoMysql implements ExternalDocumentDao {

    private static final String GET_EXTERNAL_DOCUMENT_WITH_ID =
            "SELECT * FROM external_document WHERE ext_document_id = ?";

    private static final String UPDATE_EXTERNAL_DOCUMENT =
            "UPDATE external_document SET title = ?, html = ?, module_id = ? WHERE ext_document_id = ?";

    private static final String INSERT_EXTERNAL_DOCUMENT =
            "INSERT INTO external_document(title, html, module_id) VALUES (?, ?, ?)";

    private final RowMapper<ExternalDocument> rowMapper = (resultSet, i) -> new ExternalDocument(
            resultSet.getInt("ext_document_id"),
            resultSet.getString("title"),
            resultSet.getString("html"),
            new ModuleId(resultSet.getString("module_id")));

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public ExternalDocumentDaoMysql(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public ExternalDocument get(int id) {
        return jdbcTemplate.query(GET_EXTERNAL_DOCUMENT_WITH_ID, new RowMapperResultSetExtractor<>(rowMapper), id);
    }

    @Override
    public int update(ExternalDocument externalDocument) {
        checkNotNull(externalDocument.getExternalModule());
        return jdbcTemplate.update(UPDATE_EXTERNAL_DOCUMENT, externalDocument.getTitle(), externalDocument.getHtml(),
                externalDocument.getExternalModule().raw(), externalDocument.getId());
    }

    @Override
    public int create(ExternalDocument externalDocument) {
        checkNotNull(externalDocument.getExternalModule());
        KeyHolder keyHolder = new GeneratedKeyHolder();
        int numRows = jdbcTemplate.update(connection -> {
            PreparedStatement pst = connection.prepareStatement(INSERT_EXTERNAL_DOCUMENT, new String[]{"ext_document_id"});
            pst.setString(1, externalDocument.getTitle());
            pst.setString(2, externalDocument.getHtml());
            pst.setString(3, externalDocument.getExternalModule().raw());
            return pst;
        }, keyHolder);
        externalDocument.setId(keyHolder.getKey().intValue());
        return numRows;
    }
}
