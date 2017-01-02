package de.fh_bielefeld.newsboard.dao.mysql;

import de.fh_bielefeld.newsboard.dao.ExternDocumentDao;
import de.fh_bielefeld.newsboard.dao.ExternModuleDao;
import de.fh_bielefeld.newsboard.model.ExternDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by felixmeyer on 17.12.16.
 */
@Component
public class ExternDocumentDaoImpl implements ExternDocumentDao {
    public String GET_EXTERN_DOCUMENT_WITH_ID =
            "SELECT extern_document.title, extern_document.html, extern_document.module_id, " +
                    "extern_module.name, extern_module.author, extern_module.description " +
                    "FROM extern_document INNER JOIN extern_module ON extern_document.module_id = extern_module.id " +
                    "WHERE extern_document.id = ?";

    @Autowired
    private ExternModuleDao externModuleDao;

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public ExternDocumentDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public ExternDocument getExternDocumentWithId(int id) {
        return null;
    }

    @Override
    public List<ExternDocument> getAllExternDocumentsWithoutData() {
        return null;
    }

    @Override
    public int updateExternDocument(ExternDocument externDocument) {
        return 0;
    }

    @Override
    public int insertExternDocument(ExternDocument externDocument) {
        return 0;
    }
}
