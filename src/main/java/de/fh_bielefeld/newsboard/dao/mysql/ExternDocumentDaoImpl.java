package de.fh_bielefeld.newsboard.dao.mysql;

import de.fh_bielefeld.newsboard.dao.ExternDocumentDao;
import de.fh_bielefeld.newsboard.dao.ExternModuleDao;
import de.fh_bielefeld.newsboard.model.ExternDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by felixmeyer on 17.12.16.
 */
@Component
public class ExternDocumentDaoImpl implements ExternDocumentDao {
    public String GET_EXTERN_DOCUMENT_WITH_ID =
            "SELECT * FROM extern_document WHERE id = ?";
    public String UPDATE_EXTERN_DOCUMENT =
            "UPDATE extern_document SET title = ?, html = ?, module_id = ? WHERE id = ?";
    public String INSERT_EXTERN_DOCUMENT =
            "INSERT INTO extern_document(title, html, module_id) VALUES (?, ?, ?)";

    @Autowired
    private ExternModuleDao externModuleDao;

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public ExternDocumentDaoImpl(JdbcTemplate jdbcTemplate, ExternModuleDao externModuleDao) {
        this.jdbcTemplate = jdbcTemplate;
        this.externModuleDao = externModuleDao;
    }

    @Override
    public ExternDocument getExternDocumentWithId(int id) {
        ExternDocumentDatabaseObject rawExternDocument = jdbcTemplate.queryForObject(
                GET_EXTERN_DOCUMENT_WITH_ID,
                new ExternDocumentDatabaseObjectRowMapper(),
                id);

        ExternDocument externDocument = new ExternDocument();
        externDocument.setId(rawExternDocument.getId());
        externDocument.setTitle(rawExternDocument.getTitle());
        externDocument.setHtml(rawExternDocument.getHtml());
        String externModuleId = rawExternDocument.getModuleId();
        if (externModuleId == null) {
            externDocument.setExternModule(null);
        } else {
            externDocument.setExternModule(externModuleDao.getExternModuleWithId(externModuleId));
        }

        return externDocument;
    }

    @Override
    public int updateExternDocument(ExternDocument externDocument) {
        String moduleId = externDocument.getExternModule() == null ? null : externDocument.getExternModule().getId();
        Object[] attributes = {
                externDocument.getTitle(),
                externDocument.getHtml(),
                moduleId,
                externDocument.getId()
        };

        return jdbcTemplate.update(UPDATE_EXTERN_DOCUMENT, attributes);
    }

    @Override
    public int insertExternDocument(ExternDocument externDocument) {
        final String moduleId = externDocument.getExternModule() == null ? null : externDocument.getExternModule().getId();
        KeyHolder keyHolder = new GeneratedKeyHolder();
        int numRows = jdbcTemplate.update(new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                PreparedStatement pst =
                        connection.prepareStatement(INSERT_EXTERN_DOCUMENT, new String[]{"id"});
                pst.setString(1, externDocument.getTitle());
                pst.setString(2, externDocument.getHtml());
                pst.setString(3, moduleId);
                return pst;
            }
        });
        externDocument.setId(keyHolder.getKey().intValue());
        return numRows;
    }

    protected class ExternDocumentDatabaseObjectRowMapper implements RowMapper<ExternDocumentDatabaseObject> {
        @Override
        public ExternDocumentDatabaseObject mapRow(ResultSet resultSet, int i) throws SQLException {
            ExternDocumentDatabaseObject externDocument = new ExternDocumentDatabaseObject();

            externDocument.setId(resultSet.getInt("id"));
            externDocument.setTitle(resultSet.getString("title"));
            externDocument.setHtml(resultSet.getString("html"));
            externDocument.setModuleId(resultSet.getString("module_id"));

            return externDocument;
        }
    }

    protected class ExternDocumentDatabaseObject {
        private Integer id;
        private String title;
        private String html;
        private String moduleId;

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getHtml() {
            return html;
        }

        public void setHtml(String html) {
            this.html = html;
        }

        public String getModuleId() {
            return moduleId;
        }

        public void setModuleId(String moduleId) {
            this.moduleId = moduleId;
        }
    }

}
