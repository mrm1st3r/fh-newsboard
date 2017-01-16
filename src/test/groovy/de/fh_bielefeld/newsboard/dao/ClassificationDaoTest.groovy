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
    ClassificationDao classificationDao
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
    ExternModule dummyModule

    def "test insertion"() {
        when:
        Classification classification = new Classification()
        classification.setConfidence(1.0123456789)
        classification.setValue(2.0123456789)
        classification.setExternModule(dummyModule)
        List<Sentence> dummySentences = dummyDocument.getSentences()
        classification.setSentenceId(dummySentences.get(0).getId())

        then:
        classificationDao.insertClassification(classification)

        Classification testClassification = classificationDao.getClassification(dummySentences.get(0), dummyModule)
        testClassification.getConfidence() == classification.getConfidence()
        testClassification.getValue() == classification.getValue()
        testClassification.getSentenceId() == classification.getSentenceId()
        testClassification.getExternModule() == classification.getExternModule()

        noExceptionThrown()
    }

    def "test updating"() {
        when:
        Classification classification = new Classification()
        classification.setConfidence(1.0123456789)
        classification.setValue(2.0123456789)
        classification.setExternModule(dummyModule)
        List<Sentence> dummySentences = dummyDocument.getSentences()
        classification.setSentenceId(dummySentences.get(0).getId())
        classificationDao.insertClassification(classification)

        classification.setConfidence(2.0123456789)
        classification.setValue(1.0123456789)

        then:
        classificationDao.updateClassification(classification)

        Classification testClassification = classificationDao.getClassification(dummySentences.get(0), dummyModule)
        testClassification.getConfidence() == classification.getConfidence()
        testClassification.getValue() == classification.getValue()
        testClassification.getSentenceId() == classification.getSentenceId()
        testClassification.getExternModule() == classification.getExternModule()

        noExceptionThrown()
    }

    def "test selection with sentence and module"() {
        when:
        Classification classification = new Classification()
        classification.setConfidence(1.0123456789)
        classification.setValue(2.0123456789)
        classification.setExternModule(dummyModule)
        List<Sentence> dummySentences = dummyDocument.getSentences()
        classification.setSentenceId(dummySentences.get(0).getId())
        classificationDao.insertClassification(classification)

        then:
        Classification testClassification = classificationDao.getClassification(dummySentences.get(0), dummyModule)

        testClassification.getConfidence() == classification.getConfidence()
        testClassification.getValue() == classification.getValue()
        testClassification.getSentenceId() == classification.getSentenceId()
        testClassification.getExternModule() == classification.getExternModule()

        noExceptionThrown()
    }

    def "test selection with sentence only"() {
        when:
        Classification classification = new Classification()
        classification.setConfidence(1.0123456789)
        classification.setValue(2.0123456789)
        classification.setExternModule(dummyModule)
        List<Sentence> dummySentences = dummyDocument.getSentences()
        classification.setSentenceId(dummySentences.get(0).getId())
        classificationDao.insertClassification(classification)

        ExternModule additionalModule = getNewExternModule()
        additionalModule.setId("additional_testing_module")
        insertExternModule(additionalModule)

        classification.setExternModule(additionalModule)
        classificationDao.insertClassification(classification)


        then:
        List<Classification> testClassifications = classificationDao.getAllClassificationsForSentence(dummySentences.get(0))

        for (Classification c : testClassifications) {
            c.getConfidence() == classification.getConfidence()
            c.getValue() == classification.getValue()
            c.getSentenceId() == classification.getSentenceId()
        }
        testClassifications.get(0).getExternModule().getId() == "additional_testing_module"
        testClassifications.get(1).getExternModule().getId() == "test_module"

        noExceptionThrown()
    }

    def "test selection with module only"() {
        when:
        Classification classification = new Classification()
        classification.setConfidence(1.0123456789)
        classification.setValue(2.0123456789)
        classification.setExternModule(dummyModule)
        List<Sentence> dummySentences = dummyDocument.getSentences()
        classification.setSentenceId(dummySentences.get(0).getId())
        classificationDao.insertClassification(classification)

        ExternModule additionalModule = getNewExternModule()
        additionalModule.setId("additional_testing_module")
        insertExternModule(additionalModule)

        classification.setExternModule(additionalModule)
        classification.setSentenceId(dummySentences.get(1).getId())
        classificationDao.insertClassification(classification)
        classification.setSentenceId(dummySentences.get(2).getId())
        classificationDao.insertClassification(classification)


        then:
        List<Classification> testClassifications = classificationDao.getAllClassificationsFromModule(additionalModule)

        testClassifications.size() == 2
        for (Classification c : testClassifications) {
            c.getConfidence() == classification.getConfidence()
            c.getValue() == classification.getValue()
            c.getSentenceId() == classification.getSentenceId()
        }

        noExceptionThrown()
    }

    def setup() {
        moduleIds = new ArrayList<String>()
        documentIds = new ArrayList<Integer>()
        sentenceIds = new ArrayList<Integer>()

        ExternModule module = getNewExternModule()
        Document document = getNewDocument(module)
        insertExternModule(module)
        insertDocument(document)

        dummyModule = module
        dummyDocument = document
    }

    def cleanup() {
        for (Object[] id : classificationIds) {
            jdbcTemplate.update(
                    "DELETE FROM classification " +
                            "WHERE sentence_id = " + id[0]
                            + " AND module_id = '" + id[1] + "'")
        }
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

    def insertExternModule(ExternModule module) {
        externModuleDao.insertExternModule(module)
        moduleIds.add(module.getId())
    }

    def insertDocument(Document document) {
        documentDao.insertDocumentWithSentences(document)
        documentIds.add(document.getId())
        for (Sentence s : document.getSentences()) {
            sentenceIds.add(s.getId())
        }
    }

    def getNewDocument(ExternModule module) {
        Document document = new Document()
        DocumentMetaData metaData = new DocumentMetaData()
        metaData.setAuthor("Test author")
        metaData.setCrawlTime(Calendar.getInstance())
        metaData.setCreationTime(Calendar.getInstance())
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
        ExternModule module = new ExternModule()
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
