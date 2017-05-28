package de.fhbielefeld.newsboard.dao

import de.fhbielefeld.newsboard.NewsboardApplication
import de.fhbielefeld.newsboard.TestUtils
import de.fhbielefeld.newsboard.model.Document
import de.fhbielefeld.newsboard.model.ExternalModule
import de.fhbielefeld.newsboard.model.Sentence
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
        Document document = TestUtils.sampleDocumentForDb(module)
        accessDao.create(TestUtils.sampleAccess())
        externModuleDao.create(module)

        dummyModule = module
        dummyDocument = document
    }

    def "should create and find for document"() {
        given:
        documentDao.create(dummyDocument)
        def dummySentences = dummyDocument.getSentences()

        when:
        List<Sentence> testSentences = sentenceDao.findForDocument(dummyDocument)

        then:
        testSentences.size() == dummySentences.size()
        for ( int i = 0; i < testSentences.size(); i++) {
            testSentences[i].getNumber() == dummySentences[i].getNumber()
            testSentences[i].getText() == dummySentences[i].getText()
        }
    }

    def cleanup() {
        TestUtils.cleanupDatabase(jdbcTemplate, sentenceIds, [dummyDocument.getId()], [dummyModule.getId()])
    }
}
