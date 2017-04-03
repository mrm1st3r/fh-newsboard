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
    @Autowired
    AccessDao accessDao

    List<Integer> sentenceIds

    Document dummyDocument
    ExternalModule dummyModule

    def setup() {
        sentenceIds = new ArrayList<Integer>()

        ExternalModule module = TestUtils.sampleModule()
        Document document = getNewDocument(module)
        accessDao.create(TestUtils.sampleAccess())
        externModuleDao.create(module)
        documentDao.create(document)

        dummyModule = module
        dummyDocument = document
    }

    def "test selection with document"() {
        given:
        Sentence sentence
        for (int i = 0; i < 3; i++) {
            sentence = TestUtils.sampleSentence()
            sentenceDao.create(sentence, dummyDocument)
            sentenceIds.add(sentence.getId())
        }
        when:
        List<Sentence> testSentences = sentenceDao.findForDocument(dummyDocument)

        then:
        testSentences.size() == 3
        for (Sentence testSentence : testSentences) {
            testSentence.getId() == sentence.getId()
            testSentence.getNumber() == sentence.getNumber()
            testSentence.getText() == sentence.getText()
        }
    }

    def cleanup() {
        TestUtils.cleanupDatabase(jdbcTemplate, sentenceIds, [dummyDocument.getId()], [dummyModule.getId()])
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
