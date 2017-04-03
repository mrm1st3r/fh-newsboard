package de.fh_bielefeld.newsboard.dao

import de.fh_bielefeld.newsboard.NewsboardApplication
import de.fh_bielefeld.newsboard.TestUtils
import de.fh_bielefeld.newsboard.model.Document
import de.fh_bielefeld.newsboard.model.DocumentMetaData
import de.fh_bielefeld.newsboard.model.DocumentStub
import de.fh_bielefeld.newsboard.model.ExternalModule
import de.fh_bielefeld.newsboard.model.Sentence
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
        dummyDocument = getNewDocument(dummyModule)
        accessDao.create(TestUtils.sampleAccess())
        externModuleDao.create(dummyModule)
        moduleIds.add(dummyModule.getId())
    }

    def "test insertion"() {
        when:
        Document document = getNewDocument(dummyModule)
        documentDao.create(document)
        documentIds.add(document.getId())

        then:
        Document testDocument = documentDao.get(document.getId())
        compareDocuments(testDocument, document)
    }

    def "test insertion with sentences"() {
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

    def "test selection with id"() {
        when:
        insertDocument(dummyDocument)

        then:
        Document testDocument = documentDao.get(dummyDocument.getId())
        compareDocuments(testDocument, dummyDocument)
    }

    def "test selection only with metadata"() {
        when:
        insertDocument(getNewDocument(dummyModule))
        insertDocument(getNewDocument(dummyModule))
        insertDocument(getNewDocument(dummyModule))
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
        assert thisDocument.getModule().getAuthor() == thatDocument.getModule().getAuthor()
        assert thisDocument.getModule().getDescription() == thatDocument.getModule().getDescription()
        assert thisDocument.getModule().getName() == thatDocument.getModule().getName()
    }

    def insertDocument(Document document) {
        documentDao.create(document)
        documentIds.add(document.getId())
        for (Sentence s : document.getSentences()) {
            sentenceIds.add(s.getId())
        }
    }

    def getNewDocument(ExternalModule module) {
        Document document = new Document()
        DocumentMetaData metaData = new DocumentMetaData("Test document", "Test author", "Test source",
                new GregorianCalendar(2017, 6, 4), new GregorianCalendar(2010, 2, 1),  module)
        document.setMetaData(metaData)
        for (int i = 0; i < 3; i++) {
            document.addSentence(TestUtils.sampleSentence())
        }
        return document
    }
}
