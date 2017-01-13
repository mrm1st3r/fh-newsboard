package groovy.de.fh_bielefeld.newsboard.dao

import de.fh_bielefeld.newsboard.NewsboardApplication
import de.fh_bielefeld.newsboard.dao.ExternDocumentDao
import de.fh_bielefeld.newsboard.dao.ExternModuleDao
import de.fh_bielefeld.newsboard.model.ExternDocument
import de.fh_bielefeld.newsboard.model.ExternModule
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.jdbc.core.JdbcTemplate
import spock.lang.Specification

/**
 * Created by Felix on 13.01.2017.
 */
@SpringBootTest(classes = NewsboardApplication.class)
class ExternDocumentDaoTest extends Specification {

    @Autowired
    JdbcTemplate jdbcTemplate;
    @Autowired
    ExternDocumentDao dao;
    @Autowired
    ExternModuleDao externModuleDao;

    def "test insertion"() {
        when:
        ExternDocument doc = getNewExternDocument();

        then:
        dao.insertExternDocument(doc);

        ExternDocument testingDoc = getLatestAddedDocument();
        testingDoc.getExternModule() == doc.getExternModule();
        testingDoc.getHtml() == doc.getHtml();
        testingDoc.getTitle() == doc.getTitle();

        noExceptionThrown();
    }

    def "test updating"() {
        setup:
        ExternModule additionalModule = getNewExternModule();
        additionalModule.setId("extern_module_testing2");
        externModuleDao.insertExternModule(additionalModule);

        when:
        ExternDocument doc = getNewExternDocument();
        dao.insertExternDocument(doc);
        doc = getLatestAddedDocument();
        doc.setTitle("New title");
        doc.setHtml("new html");
        doc.setExternModule(additionalModule);

        then:
        dao.updateExternDocument(doc);

        ExternDocument testingDoc = getLatestAddedDocument();
        testingDoc.getTitle() == doc.getTitle();
        testingDoc.getHtml() == doc.getHtml();
        testingDoc.getExternModule().getId() == doc.getExternModule().getId();
        testingDoc.getExternModule().getName() == doc.getExternModule().getName();
        testingDoc.getExternModule().getAuthor() == doc.getExternModule().getAuthor();
        testingDoc.getExternModule().getDescription() == doc.getExternModule().getDescription();

        noExceptionThrown();
    }

    def "test selection with id"() {
        when:
        ExternDocument doc = getNewExternDocument();
        dao.insertExternDocument(doc);
        ExternDocument selectedDoc = getLatestAddedDocument();

        then:
        ExternDocument testingDoc = dao.getExternDocumentWithId(selectedDoc.getId());

        testingDoc.getId() == selectedDoc.getId();
        testingDoc.getExternModule() == selectedDoc.getExternModule();
        testingDoc.getHtml() == selectedDoc.getHtml();
        testingDoc.getTitle() == selectedDoc.getTitle();

        noExceptionThrown();
    }

    def getNewExternModule() {
        ExternModule externModule = new ExternModule();
        externModule.setId("text_module");
        externModule.setAuthor("tester");
        externModule.setDescription("Extern module only for testing purpose");
        externModule.setName("Test extern module");
        return externModule;
    }

    def getNewExternDocument() {
        ExternDocument doc = new ExternDocument();
        doc.setExternModule(getNewExternModule());
        doc.setHtml("<body>test document</body>");
        doc.setTitle("Test document");
        return doc;
    }

    def getLatestAddedDocument() {
        Integer id = jdbcTemplate.queryForObject("SELECT id FROM extern_document", Integer.class);
        ExternDocument testingDoc = dao.getExternDocumentWithId(id);
        return testingDoc;
    }

    def setup() {
        externModuleDao.insertExternModule(getNewExternModule());
    }

    def cleanup() {
        jdbcTemplate.update("DELETE FROM extern_document");
        jdbcTemplate.update("DELETE FROM extern_module");
    }
}
