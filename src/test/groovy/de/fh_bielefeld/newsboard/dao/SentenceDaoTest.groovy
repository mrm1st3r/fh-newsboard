package groovy.de.fh_bielefeld.newsboard.dao

import de.fh_bielefeld.newsboard.NewsboardApplication
import de.fh_bielefeld.newsboard.dao.DocumentDao
import de.fh_bielefeld.newsboard.dao.ExternModuleDao
import de.fh_bielefeld.newsboard.dao.SentenceDao
import de.fh_bielefeld.newsboard.model.Document
import de.fh_bielefeld.newsboard.model.DocumentMetaData
import de.fh_bielefeld.newsboard.model.ExternModule
import de.fh_bielefeld.newsboard.model.Sentence
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.jdbc.core.JdbcTemplate
import spock.lang.Specification

/**
 * Created by Felix on 13.01.2017.
 */
@SpringBootTest(classes = NewsboardApplication.class)
class SentenceDaoTest extends Specification {

    @Autowired
    DocumentDao docDao
    @Autowired
    ExternModuleDao extModDao
    @Autowired
    SentenceDao dao
    @Autowired
    JdbcTemplate jdbcTemplate

    Document doc

    def "test insertion"() {
        when:
        Sentence sentence = getNewSentence()

        then:
        dao.insertSentence(sentence, doc)

        Sentence selectedSentence = dao.getAllSentencesInDocument(doc).get(0)
        selectedSentence.getClassifications() == sentence.getClassifications()
        selectedSentence.getNumber() == sentence.getNumber()
        selectedSentence.getText() == sentence.getText()

        noExceptionThrown()
    }

    def "test updating without document"() {
        when:
        Sentence sentence = getNewSentence()
        dao.insertSentence(sentence, doc)
        sentence.setNumber(2)
        sentence.setText("Another sentence for testing purpose")
        Sentence selectedSentence = dao.getAllSentencesInDocument(doc).get(0)
        sentence.setId(selectedSentence.getId())

        then:
        dao.updateSentenceWithoutDocument(sentence)

        Sentence testingSentence = dao.getAllSentencesInDocument(doc).get(0)
        testingSentence.getClassifications() == sentence.getClassifications()
        testingSentence.getNumber() == sentence.getNumber()
        testingSentence.getText() == sentence.getText()

        noExceptionThrown()
    }

    def "test selection with document"() {
        when:
        Sentence sentence = getNewSentence()
        dao.insertSentence(sentence, doc)
        dao.insertSentence(sentence, doc)
        dao.insertSentence(sentence, doc)

        then:
        List<Sentence> sentences = dao.getAllSentencesInDocument(doc)

        sentences.size() == 3
        for (Sentence s : sentences) {
            s.getNumber() == sentence.getNumber()
            s.getText() == sentence.getText()
        }

        noExceptionThrown()
    }

    def "test selection with id"() {
        when:
        Sentence sentence = getNewSentence()
        dao.insertSentence(sentence, doc)
        Integer id = dao.getAllSentencesInDocument(doc).get(0).getId()

        then:
        Sentence testSentence = dao.getSentenceWithId(id)

        testSentence.getText() == sentence.getText()
        testSentence.getNumber() == sentence.getNumber()

        noExceptionThrown()
    }

    def getNewSentence() {
        Sentence sentence = new Sentence()
        sentence.setNumber(1)
        sentence.setText("Test sentence")
        return sentence
    }

    def getNewExternModule() {
        ExternModule externModule = new ExternModule()
        externModule.setId("text_module")
        externModule.setAuthor("tester")
        externModule.setDescription("Extern module only for testing purpose")
        externModule.setName("Test extern module")
        return externModule
    }

    def setup() {
        ExternModule module = getNewExternModule()
        extModDao.insertExternModule(module)
        doc = new Document()
        DocumentMetaData docMd = new DocumentMetaData()
        docMd.setModule(module)
        docMd.setTitle("Test document")
        doc.setMetaData(docMd)
        docDao.insertDocument(doc)
        Integer docId = jdbcTemplate.queryForObject("SELECT id FROM document", Integer.class)
        doc.setId(docId)
    }

    def cleanup() {
        jdbcTemplate.update("DELETE FROM document")
        jdbcTemplate.update("DELETE FROM extern_module")
        jdbcTemplate.update("DELETE FROM sentence")
        jdbcTemplate.update("DELETE FROM classification")
    }
}
