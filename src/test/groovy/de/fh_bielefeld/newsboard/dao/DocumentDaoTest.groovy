package de.fh_bielefeld.newsboard.dao

import de.fh_bielefeld.newsboard.NewsboardApplication
import de.fh_bielefeld.newsboard.model.Document
import de.fh_bielefeld.newsboard.model.DocumentMetaData
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

    List<String> moduleIds
    List<Integer> documentIds
    List<Integer> sentenceIds

    Document dummyDocument
    ExternalModule dummyModule

    def "test insertion"() {
        when:
        Document document = getNewDocument(getNewExternModule())
        documentDao.create(document)
        documentIds.add(document.getId())

        then:
        Document testDocument = documentDao.get(document.getId())
        testDocument.getId() == document.getId()
        testDocument.getAuthor() == document.getAuthor()
        testDocument.getCrawlTime().getTimeInMillis() == document.getCrawlTime().getTimeInMillis()
        testDocument.getCreationTime().getTimeInMillis() == document.getCreationTime().getTimeInMillis()
        testDocument.getSource() == document.getSource()
        testDocument.getTitle() == document.getTitle()
        testDocument.getModule().getId() == document.getModule().getId()
        testDocument.getModule().getAuthor() == document.getModule().getAuthor()
        testDocument.getModule().getDescription() == document.getModule().getDescription()
        testDocument.getModule().getName() == document.getModule().getName()

        noExceptionThrown()
    }

    def "test insertion with sentences"() {
        when:
        insertDocument(dummyDocument)

        then:
        Document testDocument = documentDao.get(dummyDocument.getId())
        testDocument.getId() == dummyDocument.getId()
        testDocument.getAuthor() == dummyDocument.getAuthor()
        testDocument.getCrawlTime().getTimeInMillis() == dummyDocument.getCrawlTime().getTimeInMillis()
        testDocument.getCreationTime().getTimeInMillis() == dummyDocument.getCreationTime().getTimeInMillis()
        testDocument.getSource() == dummyDocument.getSource()
        testDocument.getTitle() == dummyDocument.getTitle()

        testDocument.getSentences().size() == dummyDocument.getSentences().size()
        for (int i = 0; i < testDocument.getSentences().size(); i++) {
            testDocument.getSentences().get(i).getId() == dummyDocument.getSentences().get(i).getId()
            testDocument.getSentences().get(i).getNumber() == dummyDocument.getSentences().get(i).getNumber()
            testDocument.getSentences().get(i).getText() == dummyDocument.getSentences().get(i).getText()
        }

        noExceptionThrown()
    }

    def "test updating"() {
        when:
        insertDocument(dummyDocument)
        DocumentMetaData metaData = new DocumentMetaData()
        metaData.setTitle("Test document again")
        metaData.setSource("Test source again")
        metaData.setAuthor("Test author again")
        metaData.setCrawlTime(dummyDocument.getCreationTime())
        metaData.setCreationTime(dummyDocument.getCrawlTime())
        ExternalModule externModule =  getNewExternModule()
        externModule.setId("test_module_again")
        insertExternModule(externModule)
        metaData.setModule(externModule)
        dummyDocument.setMetaData(metaData)

        then:
        documentDao.update(dummyDocument)

        Document testDocument = documentDao.get(dummyDocument.getId())
        testDocument.getId() == dummyDocument.getId()
        testDocument.getAuthor() == dummyDocument.getAuthor()
        testDocument.getCrawlTime().getTimeInMillis() == dummyDocument.getCrawlTime().getTimeInMillis()
        testDocument.getCreationTime().getTimeInMillis() == dummyDocument.getCreationTime().getTimeInMillis()
        testDocument.getSource() == dummyDocument.getSource()
        testDocument.getTitle() == dummyDocument.getTitle()
        testDocument.getModule().getId() == dummyDocument.getModule().getId()
        testDocument.getModule().getAuthor() == dummyDocument.getModule().getAuthor()
        testDocument.getModule().getDescription() == dummyDocument.getModule().getDescription()
        testDocument.getModule().getName() == dummyDocument.getModule().getName()

        noExceptionThrown()
    }

    def "test selection with id"() {
        when:
        insertDocument(dummyDocument)

        then:
        Document testDocument = documentDao.get(dummyDocument.getId())

        testDocument.getId() == dummyDocument.getId()
        testDocument.getAuthor() == dummyDocument.getAuthor()
        testDocument.getCrawlTime().getTimeInMillis() == dummyDocument.getCrawlTime().getTimeInMillis()
        testDocument.getCreationTime().getTimeInMillis() == dummyDocument.getCreationTime().getTimeInMillis()
        testDocument.getSource() == dummyDocument.getSource()
        testDocument.getTitle() == dummyDocument.getTitle()
        testDocument.getModule().getId() == dummyDocument.getModule().getId()
        testDocument.getModule().getAuthor() == dummyDocument.getModule().getAuthor()
        testDocument.getModule().getDescription() == dummyDocument.getModule().getDescription()
        testDocument.getModule().getName() == dummyDocument.getModule().getName()

        noExceptionThrown()
    }

    def "test selection only with metadata"() {
        when:
        insertDocument(dummyDocument)
        insertDocument(dummyDocument)
        insertDocument(dummyDocument)

        then:
        List<Document> allDocuments = documentDao.findAllStubs()

        for (Document testDocument : allDocuments) {
            testDocument.getId() == dummyDocument.getId()
            testDocument.getAuthor() == dummyDocument.getAuthor()
            testDocument.getCrawlTime().getTimeInMillis() == dummyDocument.getCrawlTime().getTimeInMillis()
            testDocument.getCreationTime().getTimeInMillis() == dummyDocument.getCreationTime().getTimeInMillis()
            testDocument.getSource() == dummyDocument.getSource()
            testDocument.getTitle() == dummyDocument.getTitle()
            testDocument.getModule().getId() == dummyDocument.getModule().getId()
            testDocument.getModule().getAuthor() == dummyDocument.getModule().getAuthor()
            testDocument.getModule().getDescription() == dummyDocument.getModule().getDescription()
            testDocument.getModule().getName() == dummyDocument.getModule().getName()
            testDocument.getSentences().size() == 0
        }

        noExceptionThrown()
    }

    def setup() {
        moduleIds = new ArrayList<String>()
        documentIds = new ArrayList<Integer>()
        sentenceIds = new ArrayList<Integer>()

        ExternalModule module = getNewExternModule()
        Document document = getNewDocument(module)
        insertExternModule(module)
        insertDocument(document)

        dummyModule = module
        dummyDocument = document
    }

    def cleanup() {
        for (Integer id : sentenceIds) {
            jdbcTemplate.update("DELETE FROM sentence WHERE id = " + id)
        }
        for (Integer id : documentIds) {
            jdbcTemplate.update("DELETE FROM document WHERE id = " + id)
        }
        for(String id : moduleIds) {
            jdbcTemplate.update("DELETE FROM extern_module WHERE id = '" + id + "'")
        }
    }

    def insertExternModule(ExternalModule module) {
        externModuleDao.create(module)
        moduleIds.add(module.getId())
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
        DocumentMetaData metaData = new DocumentMetaData()
        metaData.setAuthor("Test author")
        metaData.setCrawlTime(new GregorianCalendar(2010, 2, 1))
        metaData.setCreationTime(new GregorianCalendar(2017, 6, 4))
        metaData.setModule(module)
        metaData.setSource("Test source")
        metaData.setTitle("Test document")
        metaData.setModule(module)
        document.setMetaData(metaData)
        for (int i = 0; i < 3; i++) {
            document.addSentence(getNewSentence())
        }
        return document
    }

    def getNewExternModule() {
        ExternalModule module = new ExternalModule()
        module.setId("test_module")
        module.setAuthor("Tester")
        module.setDescription("Module for testing purpose")
        module.setName("Test module")
        return module
    }

    def getNewSentence() {
        Sentence sentence = new Sentence()
        sentence.setNumber(1)
        sentence.setText("Example text of a sentence object for testing purposes.")
        return sentence
    }
}
