package groovy.de.fh_bielefeld.newsboard.dao

import de.fh_bielefeld.newsboard.NewsboardApplication
import de.fh_bielefeld.newsboard.dao.ClassificationDao
import de.fh_bielefeld.newsboard.dao.DocumentDao
import de.fh_bielefeld.newsboard.dao.ExternModuleDao
import de.fh_bielefeld.newsboard.dao.SentenceDao
import de.fh_bielefeld.newsboard.model.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.jdbc.core.JdbcTemplate
import spock.lang.Specification

/**
 * Created by Felix on 14.01.2017.
 */
@SpringBootTest(classes = NewsboardApplication)
class ClassificationDaoTest extends Specification {

    @Autowired
    JdbcTemplate jdbcTemplate
    @Autowired
    ClassificationDao dao
    @Autowired
    DocumentDao documentDao
    @Autowired
    SentenceDao sentenceDao
    @Autowired
    ExternModuleDao externModuleDao

    Document referenceDocument
    Sentence referenceSentence
    ExternModule referenceModule

    def "test insertion"() {
        when:
        Classification classification = new Classification()
        classification.setExternModule(referenceModule)
        classification.setSentenceId(referenceSentence.getId())
        classification.setDocumentId(referenceDocument.getId())
        classification.setValue(1.0123456789)
        classification.setConfidence(2.0123456789)

        then:
        dao.insertClassification(classification)
        Classification testClassification = dao.getClassification(referenceSentence, referenceDocument, referenceModule)

        testClassification.getConfidence() == classification.getConfidence()
        testClassification.getValue() == classification.getValue()
        testClassification.getExternModule().getId() == classification.getExternModule().getId()
        testClassification.getExternModule().getAuthor() == classification.getExternModule().getAuthor()
        testClassification.getExternModule().getDescription() == classification.getExternModule().getDescription()
        testClassification.getExternModule().getName() == classification.getExternModule().getName()
        testClassification.getSentenceId() == classification.getSentenceId()
        testClassification.getDocumentId() == classification.getDocumentId()

        noExceptionThrown()
    }

    def "test updating"() {
        when:
        Classification classification = new Classification()
        classification.setExternModule(referenceModule)
        classification.setSentenceId(referenceSentence.getId())
        classification.setDocumentId(referenceDocument.getId())
        classification.setValue(1.0123456789)
        classification.setConfidence(2.0123456789)
        dao.insertClassification(classification)

        classification.setConfidence(1.0123456789)
        classification.setValue(2.0123456789)

        then:
        dao.updateClassification(classification)
        Classification testClassification = dao.getClassification(referenceSentence, referenceDocument, referenceModule)

        testClassification.getConfidence().getAsDouble() == classification.getValue()
        testClassification.getValue() == classification.getConfidence().getAsDouble()
        testClassification.getExternModule().getId() == classification.getExternModule().getId()
        testClassification.getExternModule().getAuthor() == classification.getExternModule().getAuthor()
        testClassification.getExternModule().getDescription() == classification.getExternModule().getDescription()
        testClassification.getExternModule().getName() == classification.getExternModule().getName()
        testClassification.getSentenceId() == classification.getSentenceId()
        testClassification.getDocumentId() == classification.getDocumentId()

        noExceptionThrown()
    }

    def "test selection with sentence"() {
        when:
        Classification classification = new Classification()
        classification.setExternModule(referenceModule)
        classification.setSentenceId(referenceSentence.getId())
        classification.setDocumentId(referenceDocument.getId())
        classification.setValue(1.0123456789)
        classification.setConfidence(2.0123456789)
        dao.insertClassification(classification)

        then:
        List<Classification> allClassifications = dao.getAllClassificationsForSentence(referenceSentence)

        allClassifications.size() == 1
        for (Classification testClassification : allClassifications) {
            testClassification.getConfidence().getAsDouble() == classification.getValue()
            testClassification.getValue() == classification.getConfidence().getAsDouble()
            testClassification.getExternModule().getId() == classification.getExternModule().getId()
            testClassification.getExternModule().getAuthor() == classification.getExternModule().getAuthor()
            testClassification.getExternModule().getDescription() == classification.getExternModule().getDescription()
            testClassification.getExternModule().getName() == classification.getExternModule().getName()
            testClassification.getSentenceId() == classification.getSentenceId()
            testClassification.getDocumentId() == classification.getDocumentId()
        }

        noExceptionThrown()
    }

    def "test selection with document"() {
        when:
        Classification classification = new Classification()
        classification.setExternModule(referenceModule)
        classification.setSentenceId(referenceSentence.getId())
        classification.setDocumentId(referenceDocument.getId())
        classification.setValue(1.0123456789)
        classification.setConfidence(2.0123456789)
        dao.insertClassification(classification)

        then:
        List<Classification> allClassifications = dao.getAllClassificationsForDocument(referenceDocument)

        allClassifications.size() == 1
        for (Classification testClassification : allClassifications) {
            testClassification.getConfidence().getAsDouble() == classification.getValue()
            testClassification.getValue() == classification.getConfidence().getAsDouble()
            testClassification.getExternModule().getId() == classification.getExternModule().getId()
            testClassification.getExternModule().getAuthor() == classification.getExternModule().getAuthor()
            testClassification.getExternModule().getDescription() == classification.getExternModule().getDescription()
            testClassification.getExternModule().getName() == classification.getExternModule().getName()
            testClassification.getSentenceId() == classification.getSentenceId()
            testClassification.getDocumentId() == classification.getDocumentId()
        }

        noExceptionThrown()
    }

    def "test selection with extern module"() {
        when:
        Classification classification = new Classification()
        classification.setExternModule(referenceModule)
        classification.setSentenceId(referenceSentence.getId())
        classification.setDocumentId(referenceDocument.getId())
        classification.setValue(1.0123456789)
        classification.setConfidence(2.0123456789)
        dao.insertClassification(classification)

        then:
        List<Classification> allClassifications = dao.getAllClassificationsFromModule(referenceModule)

        allClassifications.size() == 1
        for (Classification testClassification : allClassifications) {
            testClassification.getConfidence().getAsDouble() == classification.getValue()
            testClassification.getValue() == classification.getConfidence().getAsDouble()
            testClassification.getExternModule().getId() == classification.getExternModule().getId()
            testClassification.getExternModule().getAuthor() == classification.getExternModule().getAuthor()
            testClassification.getExternModule().getDescription() == classification.getExternModule().getDescription()
            testClassification.getExternModule().getName() == classification.getExternModule().getName()
            testClassification.getSentenceId() == classification.getSentenceId()
            testClassification.getDocumentId() == classification.getDocumentId()
        }

        noExceptionThrown()
    }

    def "test selection with sentence, document and extern module"() {
        when:
        Classification classification = new Classification()
        classification.setExternModule(referenceModule)
        classification.setSentenceId(referenceSentence.getId())
        classification.setDocumentId(referenceDocument.getId())
        classification.setValue(1.0123456789)
        classification.setConfidence(2.0123456789)
        dao.insertClassification(classification)

        then:
        Classification testClassification = dao.getClassification(referenceSentence, referenceDocument, referenceModule)

        testClassification.getConfidence() == classification.getConfidence()
        testClassification.getValue() == classification.getValue()
        testClassification.getExternModule().getId() == classification.getExternModule().getId()
        testClassification.getExternModule().getAuthor() == classification.getExternModule().getAuthor()
        testClassification.getExternModule().getDescription() == classification.getExternModule().getDescription()
        testClassification.getExternModule().getName() == classification.getExternModule().getName()
        testClassification.getSentenceId() == classification.getSentenceId()
        testClassification.getDocumentId() == classification.getDocumentId()

        noExceptionThrown()
    }

    def setup() {
        ExternModule externModule = getExternModule()
        externModuleDao.insertExternModule(externModule)

        Document document = getDocument()
        documentDao.insertDocument(document)
        document = documentDao.getAllDocumentsOnlyWithMetaData().get(0)

        Sentence sentence = getSentence()
        sentenceDao.insertSentence(sentence, document)
        sentence = sentenceDao.getAllSentencesInDocument(document).get(0)
        document.addSentence(sentence)

        referenceDocument = document
        referenceModule = externModule
        referenceSentence = sentence
    }

    def cleanup() {
        jdbcTemplate.update("DELETE FROM classification")
        jdbcTemplate.update("DELETE FROM sentence")
        jdbcTemplate.update("DELETE FROM document")
        jdbcTemplate.update("DELETE FROM extern_module")
    }

    def getDocument() {
        Document doc = new Document()
        DocumentMetaData metaData = new DocumentMetaData()
        metaData.setTitle("Test title")
        metaData.setAuthor("Test author")
        metaData.setSource("Test source")
        metaData.setCrawlTime(getCrawlTime())
        metaData.setCreationTime(getCreationTime())
        metaData.setModule(getExternModule())
        doc.setMetaData(metaData)
        return doc
    }

    def getSentence() {
        Sentence sentence = new Sentence()
        sentence.setNumber(1)
        sentence.setText("Test sentence")
        return sentence
    }

    def getExternModule() {
        ExternModule externModule = new ExternModule()
        externModule.setId("text_module")
        externModule.setAuthor("tester")
        externModule.setDescription("Extern module only for testing purpose")
        externModule.setName("Test extern module")
        return externModule
    }

    def getCrawlTime() {
        Calendar calendar = new GregorianCalendar(2000, 1, 1)
        return calendar
    }

    def getCreationTime() {
        Calendar calendar = new GregorianCalendar(2005, 2, 2)
        return calendar
    }
}
