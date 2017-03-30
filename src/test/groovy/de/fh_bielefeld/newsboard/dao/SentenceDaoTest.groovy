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

    def setup() {
        moduleIds = new ArrayList<String>()
        documentIds = new ArrayList<Integer>()
        sentenceIds = new ArrayList<Integer>()

        ExternalModule module = TestUtils.sampleModule()
        Document document = getNewDocument(module)
        externModuleDao.create(module)
        moduleIds.add(module.getId())
        insertDocument(document)

        dummyModule = module
        dummyDocument = document
    }

    def "should insert and select"() {
        given:
        Sentence sentence = TestUtils.sampleSentence()

        when:
        sentenceDao.create(sentence, dummyDocument)
        sentenceIds.add(sentence.getId())

        then:
        compareSentences(sentenceDao.get(sentence.getId()), sentence)
    }

    def "test updating"() {
        given:
        Sentence sentence = TestUtils.sampleSentence()
        sentenceDao.create(sentence, dummyDocument)
        sentenceIds.add(sentence.getId())

        when:
        sentence.setText("Another text for testing purposes")
        sentence.setNumber(2132)
        sentenceDao.update(sentence)

        then:
        compareSentences(sentenceDao.get(sentence.getId()), sentence)
    }

    def "test selection with document"() {
        given:
        Sentence sentence = TestUtils.sampleSentence()
        sentenceDao.create(sentence, dummyDocument)
        sentenceIds.add(sentence.getId())
        sentenceDao.create(sentence, dummyDocument)
        sentenceIds.add(sentence.getId())
        sentenceDao.create(sentence, dummyDocument)
        sentenceIds.add(sentence.getId())

        when:
        List<Sentence> testSentences = sentenceDao.findForDocument(dummyDocument)

        then:
        testSentences.size() == 3
        for (Sentence testSentence : testSentences) {
            compareSentences(testSentence, sentence)
        }
    }

    def cleanup() {
        TestUtils.cleanupDatabase(jdbcTemplate, sentenceIds, documentIds, moduleIds)
    }

    def compareSentences(Sentence testSentence, Sentence sentence) {
        testSentence.getId() == sentence.getId() &&
        testSentence.getNumber() == sentence.getNumber() &&
        testSentence.getText() == sentence.getText()
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
}
