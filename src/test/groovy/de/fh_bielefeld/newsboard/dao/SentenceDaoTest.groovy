package de.fh_bielefeld.newsboard.dao

import de.fh_bielefeld.newsboard.NewsboardApplication
import de.fh_bielefeld.newsboard.TestUtils
import de.fh_bielefeld.newsboard.model.Document
import de.fh_bielefeld.newsboard.model.DocumentMetaData
import de.fh_bielefeld.newsboard.model.ExternalModule
import de.fh_bielefeld.newsboard.model.Sentence
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.jdbc.core.JdbcTemplate
import spock.lang.Specification

@SpringBootTest(classes = NewsboardApplication.class)
class SentenceDaoTest extends Specification {
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
        Sentence sentence = new Sentence()
        sentence.setNumber(1)
        sentence.setText("Text for testing purposes")

        then:
        sentenceDao.create(sentence, dummyDocument)
        sentenceIds.add(sentence.getId())

        Sentence testSentence = sentenceDao.get(sentence.getId())
        testSentence.getId() == sentence.getId()
        testSentence.getNumber() == sentence.getNumber()
        testSentence.getText() == sentence.getText()

        noExceptionThrown()
    }

    def "test updating"() {
        when:
        Sentence sentence = new Sentence()
        sentence.setNumber(1)
        sentence.setText("Text for testing purposes")
        sentenceDao.create(sentence, dummyDocument)
        sentenceIds.add(sentence.getId())
        sentence.setText("Another text for testing purposes")
        sentence.setNumber(2132)

        then:
        sentenceDao.update(sentence)

        Sentence testSentence = sentenceDao.get(sentence.getId())
        testSentence.getId() == sentence.getId()
        testSentence.getNumber() == sentence.getNumber()
        testSentence.getText() == sentence.getText()

        noExceptionThrown()
    }

    def "test selection with id"() {
        when:
        Sentence sentence = new Sentence()
        sentence.setNumber(1)
        sentence.setText("Text for testing purposes")
        sentenceDao.create(sentence, dummyDocument)
        sentenceIds.add(sentence.getId())

        then:
        Sentence testSentence = sentenceDao.get(sentence.getId())

        testSentence.getId() == sentence.getId()
        testSentence.getNumber() == sentence.getNumber()
        testSentence.getText() == sentence.getText()

        noExceptionThrown()
    }

    def "test selection with document"() {
        when:
        Sentence sentence = new Sentence()
        sentence.setNumber(1)
        sentence.setText("Text for testing purposes")
        sentenceDao.create(sentence, dummyDocument)
        sentenceIds.add(sentence.getId())
        sentenceDao.create(sentence, dummyDocument)
        sentenceIds.add(sentence.getId())
        sentenceDao.create(sentence, dummyDocument)
        sentenceIds.add(sentence.getId())


        then:
        List<Sentence> testSentences = sentenceDao.findForDocument(dummyDocument)

        testSentences.size() == 3
        for (Sentence testSentence : testSentences) {
            testSentence.getId() == sentence.getId()
            testSentence.getNumber() == sentence.getNumber()
            testSentence.getText() == sentence.getText()
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
        TestUtils.cleanupDatabase(jdbcTemplate, sentenceIds, documentIds, moduleIds)
    }

    def insertExternModule(ExternalModule module) {
        externModuleDao.create(module)
        moduleIds.add(module.getId())
    }

    def insertDocument(Document document) {
        documentDao.create(document)
        documentIds.add(document.getId())
    }

    def getNewDocument(ExternalModule module) {
        Document document = new Document()
        DocumentMetaData metaData = new DocumentMetaData("Test document", "Test author", "Test source",
                new GregorianCalendar(2017, 6, 4), new GregorianCalendar(2010, 2, 1),
                module)
        document.setMetaData(metaData)
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
}
