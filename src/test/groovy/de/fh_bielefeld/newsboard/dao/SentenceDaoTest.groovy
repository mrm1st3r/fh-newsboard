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
class SentenceDaoTest extends Specification {
    @Autowired
    JdbcTemplate jdbcTemplate
    @Autowired
    DocumentDao documentDao
    @Autowired
    SentenceDao sentenceDao
    @Autowired
    ExternModuleDao externModuleDao

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
        sentenceDao.insertSentence(sentence, dummyDocument)
        sentenceIds.add(sentence.getId())

        Sentence testSentence = sentenceDao.getSentenceWithId(sentence.getId())
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
        sentenceDao.insertSentence(sentence, dummyDocument)
        sentenceIds.add(sentence.getId())
        sentence.setText("Another text for testing purposes")
        sentence.setNumber(2132)

        then:
        sentenceDao.updateSentenceWithoutDocument(sentence)

        Sentence testSentence = sentenceDao.getSentenceWithId(sentence.getId())
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
        sentenceDao.insertSentence(sentence, dummyDocument)
        sentenceIds.add(sentence.getId())

        then:
        Sentence testSentence = sentenceDao.getSentenceWithId(sentence.getId())

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
        sentenceDao.insertSentence(sentence, dummyDocument)
        sentenceIds.add(sentence.getId())
        sentenceDao.insertSentence(sentence, dummyDocument)
        sentenceIds.add(sentence.getId())
        sentenceDao.insertSentence(sentence, dummyDocument)
        sentenceIds.add(sentence.getId())


        then:
        List<Sentence> testSentences = sentenceDao.getAllSentencesInDocument(dummyDocument)

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
        externModuleDao.insertExternModule(module)
        moduleIds.add(module.getId())
    }

    def insertDocument(Document document) {
        documentDao.insertDocument(document)
        documentIds.add(document.getId())
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
