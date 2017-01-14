package groovy.de.fh_bielefeld.newsboard.dao

import de.fh_bielefeld.newsboard.NewsboardApplication
import de.fh_bielefeld.newsboard.dao.DocumentDao
import de.fh_bielefeld.newsboard.dao.ExternModuleDao
import de.fh_bielefeld.newsboard.model.Document
import de.fh_bielefeld.newsboard.model.DocumentMetaData
import de.fh_bielefeld.newsboard.model.ExternModule
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.jdbc.core.JdbcTemplate
import spock.lang.Specification

/**
 * Created by Felix on 14.01.2017.
 */
@SpringBootTest(classes = NewsboardApplication.class)
class DocumentDaoTest extends Specification {

    @Autowired
    DocumentDao docDao;
    @Autowired
    ExternModuleDao extModDao;
    @Autowired
    JdbcTemplate jdbcTemplate;

    def "test insertion"() {
        when:
        Document doc = new Document();
        DocumentMetaData metaData = new DocumentMetaData();
        metaData.setTitle("Test title");
        metaData.setAuthor("Test author");
        metaData.setSource("Test source");
        metaData.setCrawlTime(getCrawlTime());
        metaData.setCreationTime(getCreationTime());
        metaData.setModule(getExternModule());
        doc.setMetaData(metaData);

        then:
        docDao.insertDocument(doc);

        Document testDoc = docDao.getAllDocumentsOnlyWithMetaData().get(0);
        testDoc != null;
        testDoc.getCreationTime() == metaData.getCreationTime();
        testDoc.getCrawlTime() == metaData.getCrawlTime();
        testDoc.getTitle() == metaData.getTitle();
        testDoc.getAuthor() == metaData.getAuthor();
        testDoc.getModule().getId() == testDoc.getModule().getId();
        testDoc.getModule().getAuthor() == testDoc.getModule().getAuthor();
        testDoc.getModule().getDescription() == testDoc.getModule().getDescription();
        testDoc.getModule().getName() == testDoc.getModule().getName();
        testDoc.getSource() == testDoc.getSource();

        noExceptionThrown();
    }

    def "test updating"() {
        when:
        Document doc = new Document();
        DocumentMetaData metaData = new DocumentMetaData();
        metaData.setTitle("Test title");
        metaData.setAuthor("Test author");
        metaData.setSource("Test source");
        metaData.setCrawlTime(getCrawlTime());
        metaData.setCreationTime(getCreationTime());
        metaData.setModule(getExternModule());
        doc.setMetaData(metaData);
        docDao.insertDocument(doc);

        DocumentMetaData testMetaData = new DocumentMetaData();
        testMetaData.setCrawlTime(getCreationTime());
        testMetaData.setCreationTime(getCrawlTime());
        testMetaData.setTitle("Test title2");
        testMetaData.setAuthor("Test author2");
        testMetaData.setSource("Test source2");
        testMetaData.setModule(getExternModule());
        Integer id = jdbcTemplate.queryForObject("SELECT id FROM document", Integer.class);
        doc.setId(id);
        doc.setMetaData(testMetaData);

        then:
        docDao.updateDocument(doc)

        Document testDoc = docDao.getAllDocumentsOnlyWithMetaData().get(0);
        testDoc != null;
        testDoc.getCreationTime() == doc.getCreationTime();
        testDoc.getCrawlTime() == doc.getCrawlTime();
        testDoc.getTitle() == doc.getTitle();
        testDoc.getAuthor() == doc.getAuthor();
        testDoc.getModule().getId() == doc.getModule().getId();
        testDoc.getModule().getAuthor() == doc.getModule().getAuthor();
        testDoc.getModule().getDescription() == doc.getModule().getDescription();
        testDoc.getModule().getName() == doc.getModule().getName();
        testDoc.getSource() == doc.getSource();

        noExceptionThrown();
    }

    def "test selection with id"() {
        when:
        Document doc = new Document();
        DocumentMetaData metaData = new DocumentMetaData();
        metaData.setTitle("Test title");
        metaData.setAuthor("Test author");
        metaData.setSource("Test source");
        metaData.setCrawlTime(getCrawlTime());
        metaData.setCreationTime(getCreationTime());
        metaData.setModule(getExternModule());
        doc.setMetaData(metaData);
        docDao.insertDocument(doc);
        doc = docDao.getAllDocumentsOnlyWithMetaData().get(0);

        then:
        Document testDoc = docDao.getDocumentWithId(doc.getId());

        testDoc != null;
        testDoc.getCreationTime() == doc.getCreationTime();
        testDoc.getCrawlTime() == doc.getCrawlTime();
        testDoc.getTitle() == doc.getTitle();
        testDoc.getAuthor() == doc.getAuthor();
        testDoc.getModule().getId() == doc.getModule().getId();
        testDoc.getModule().getAuthor() == doc.getModule().getAuthor();
        testDoc.getModule().getDescription() == doc.getModule().getDescription();
        testDoc.getModule().getName() == doc.getModule().getName();
        testDoc.getSource() == doc.getSource();

        noExceptionThrown();
    }

    def "test selection all documents with metadata"() {
        when:
        Document doc = new Document();
        DocumentMetaData metaData = new DocumentMetaData();
        metaData.setTitle("Test title");
        metaData.setAuthor("Test author");
        metaData.setSource("Test source");
        metaData.setCrawlTime(getCrawlTime());
        metaData.setCreationTime(getCreationTime());
        metaData.setModule(getExternModule());
        doc.setMetaData(metaData);
        docDao.insertDocument(doc);
        docDao.insertDocument(doc);
        docDao.insertDocument(doc);
        docDao.insertDocument(doc);
        docDao.insertDocument(doc);

        then:
        List<Document> allDocuments = docDao.getAllDocumentsOnlyWithMetaData();
        allDocuments.size() == 5;
        for (Document testDoc : allDocuments) {
            testDoc.getCreationTime() == doc.getCreationTime();
            testDoc.getCrawlTime() == doc.getCrawlTime();
            testDoc.getTitle() == doc.getTitle();
            testDoc.getAuthor() == doc.getAuthor();
            testDoc.getModule().getId() == doc.getModule().getId();
            testDoc.getModule().getAuthor() == doc.getModule().getAuthor();
            testDoc.getModule().getDescription() == doc.getModule().getDescription();
            testDoc.getModule().getName() == doc.getModule().getName();
            testDoc.getSource() == doc.getSource();
        }

        noExceptionThrown();
    }

    def setup() {
        ExternModule module = getExternModule();
        extModDao.insertExternModule(module);
    }

    def cleanup() {
        jdbcTemplate.update("DELETE FROM document");
        jdbcTemplate.update("DELETE FROM extern_module");
    }

    def getExternModule() {
        ExternModule externModule = new ExternModule();
        externModule.setId("text_module");
        externModule.setAuthor("tester");
        externModule.setDescription("Extern module only for testing purpose");
        externModule.setName("Test extern module");
        return externModule;
    }

    def getCrawlTime() {
        Calendar calendar = new GregorianCalendar(2000, 1, 1);
        return calendar;
    }

    def getCreationTime() {
        Calendar calendar = new GregorianCalendar(2005, 2, 2);
        return calendar;
    }
}
