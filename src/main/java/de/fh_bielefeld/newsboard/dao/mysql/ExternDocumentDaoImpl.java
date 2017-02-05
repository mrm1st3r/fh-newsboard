package de.fh_bielefeld.newsboard.dao.mysql;

import de.fh_bielefeld.newsboard.dao.ExternDocumentDao;
import de.fh_bielefeld.newsboard.dao.ExternModuleDao;
import de.fh_bielefeld.newsboard.model.ExternalDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.springframework.util.Assert.notNull;

/**
 * MySQL Implementation for ExternalDocument DAO.
 */
@Component
public class ExternDocumentDaoImpl implements ExternDocumentDao {

    private static final String GET_EXTERN_DOCUMENT_WITH_ID =
            "SELECT * FROM extern_document WHERE id = ?";

    private static final String UPDATE_EXTERN_DOCUMENT =
            "UPDATE extern_document SET title = ?, html = ?, module_id = ? WHERE id = ?";

    private static final String INSERT_EXTERN_DOCUMENT =
            "INSERT INTO extern_document(title, html, module_id) VALUES (?, ?, ?)";

    private ExternModuleDao externModuleDao;
    private JdbcTemplate jdbcTemplate;

    @Autowired
    public ExternDocumentDaoImpl(JdbcTemplate jdbcTemplate, ExternModuleDao externModuleDao) {
        this.jdbcTemplate = jdbcTemplate;
        this.externModuleDao = externModuleDao;
    }

    @Override
    public ExternalDocument getExternDocumentWithId(int id) {
        return jdbcTemplate.query(GET_EXTERN_DOCUMENT_WITH_ID, new RowMapperResultSetExtractor<>(rowMapper), id);
    }

    @Override
    public int updateExternDocument(ExternalDocument externalDocument) {
        notNull(externalDocument.getExternalModule());
        return jdbcTemplate.update(UPDATE_EXTERN_DOCUMENT, externalDocument.getTitle(), externalDocument.getHtml(),
                externalDocument.getExternalModule().getId(), externalDocument.getId());
    }

    @Override
    public int insertExternDocument(ExternalDocument externalDocument) {
        notNull(externalDocument.getExternalModule());
        KeyHolder keyHolder = new GeneratedKeyHolder();
        int numRows = jdbcTemplate.update(connection -> {
            PreparedStatement pst = connection.prepareStatement(INSERT_EXTERN_DOCUMENT, new String[]{"id"});
            pst.setString(1, externalDocument.getTitle());
            pst.setString(2, externalDocument.getHtml());
            pst.setString(3, externalDocument.getExternalModule().getId());
            return pst;
        }, keyHolder);
        externalDocument.setId(keyHolder.getKey().intValue());
        return numRows;
    }

    private RowMapper<ExternalDocument> rowMapper = new RowMapper<ExternalDocument>() {
        @Override
        public ExternalDocument mapRow(ResultSet resultSet, int i) throws SQLException {
            ExternalDocument document = new ExternalDocument();
            String externalModuleId = resultSet.getString("module_id");
            document.setId(resultSet.getInt("id"));
            document.setTitle(resultSet.getString("title"));
            document.setHtml(resultSet.getString("html"));
            document.setExternalModule(externModuleDao.getExternModuleWithId(externalModuleId));
            return document;
        }
    };
}
