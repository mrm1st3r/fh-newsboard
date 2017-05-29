package de.fhbielefeld.newsboard.dao

import de.fhbielefeld.newsboard.NewsboardApplication
import de.fhbielefeld.newsboard.TestUtils
import de.fhbielefeld.newsboard.model.access.AccessDao
import de.fhbielefeld.newsboard.model.document.*
import de.fhbielefeld.newsboard.model.module.ExternalModule
import de.fhbielefeld.newsboard.model.module.ExternalModuleDao
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.jdbc.core.JdbcTemplate
import spock.lang.Specification

@SpringBootTest(classes = NewsboardApplication.class)
class DocumentDaoTest extends Specification {
    @Autowired
    JdbcTemplate jdbcTemplate
    @Autowired
    DocumentDao documentDao
    @Autowired
    SentenceDao sentenceDao
    @Autowired
    ExternalModuleDao externModuleDao
    @Autowired
    AccessDao accessDao

    List<String> moduleIds
    List<Integer> documentIds

    List<Integer> sentenceIds
    Document dummyDocument
    ExternalModule dummyModule

    def setup() {
        moduleIds = new ArrayList<String>()
        documentIds = new ArrayList<Integer>()
        sentenceIds = new ArrayList<Integer>()

        dummyModule = TestUtils.sampleModule()
        dummyDocument = TestUtils.sampleDocumentForDb(dummyModule)
        accessDao.create(TestUtils.sampleAccess())
        externModuleDao.create(dummyModule)
        moduleIds.add(dummyModule.getId())
    }

    def "should create correctly"() {
        when:
        insertDocument(dummyDocument)

        then:
        Document testDocument = documentDao.get(dummyDocument.getId())
        compareDocuments(testDocument, dummyDocument)
        testDocument.getSentences().size() == dummyDocument.getSentences().size()
        for (int i = 0; i < testDocument.getSentences().size(); i++) {
            testDocument.getSentences().get(i).getId() == dummyDocument.getSentences().get(i).getId()
            testDocument.getSentences().get(i).getNumber() == dummyDocument.getSentences().get(i).getNumber()
            testDocument.getSentences().get(i).getText() == dummyDocument.getSentences().get(i).getText()
        }

        noExceptionThrown()
    }

    def "should find stubs"() {
        when:
        insertDocument(TestUtils.sampleDocumentForDb(dummyModule))
        insertDocument(TestUtils.sampleDocumentForDb(dummyModule))
        insertDocument(TestUtils.sampleDocumentForDb(dummyModule))
        List<DocumentStub> allDocuments = documentDao.findAllStubs()

        then:
        allDocuments.size() >= 3
        noExceptionThrown()
    }

    def cleanup() {
        TestUtils.cleanupDatabase(jdbcTemplate, sentenceIds, documentIds, moduleIds)
    }

    void compareDocuments(DocumentStub thisDocument, DocumentStub thatDocument) {
        assert thisDocument.getAuthor() == thatDocument.getAuthor()
        assert thisDocument.getCrawlTime() == thatDocument.getCrawlTime()
        assert thisDocument.getCreationTime() == thatDocument.getCreationTime()
        assert thisDocument.getSource() == thatDocument.getSource()
        assert thisDocument.getTitle() == thatDocument.getTitle()
        assert thisDocument.getModule().getId() == thatDocument.getModule().getId()
    }

    def insertDocument(Document document) {
        documentDao.create(document)
        documentIds.add(document.getId())
        for (Sentence s : document.getSentences()) {
            sentenceIds.add(s.getId())
        }
    }
}
