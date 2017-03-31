package de.fh_bielefeld.newsboard.dao

import de.fh_bielefeld.newsboard.NewsboardApplication
import de.fh_bielefeld.newsboard.TestUtils
import de.fh_bielefeld.newsboard.model.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.jdbc.core.JdbcTemplate
import spock.lang.Specification

@SpringBootTest(classes = NewsboardApplication)
class ClassificationDaoTest extends Specification {

    @Autowired
    JdbcTemplate jdbcTemplate
    @Autowired
    ClassificationDao classificationDao
    @Autowired
    DocumentDao documentDao
    @Autowired
    ExternalModuleDao externalModuleDao
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

        ExternalModule module = TestUtils.sampleModule()
        Document document = getNewDocument(module)
        accessDao.create(TestUtils.sampleAccess())
        externalModuleDao.create(module)
        moduleIds.add(module.getId())
        insertDocument(document)

        dummyModule = module
        dummyDocument = document
    }

    def "should insert and select"() {
        given:
        Sentence dummySentence = dummyDocument.getSentences().get(0)
        Classification classification = TestUtils.sampleClassification(dummyModule, dummySentence.getId())

        when:
        classificationDao.create(classification)
        Classification testClassification = classificationDao.get(dummySentence, dummyModule)

        then:
        compareClassifications(testClassification, classification)
    }

    def "test updating"() {
        given:
        Sentence dummySentence = dummyDocument.getSentences().get(0)
        Classification classification = TestUtils.sampleClassification(dummyModule, dummySentence.getId())

        when:
        classificationDao.create(classification)
        classification.setConfidence(0.5)
        classification.setValue(-1)
        classificationDao.update(classification)

        then:
        compareClassifications(classificationDao.get(dummySentence, dummyModule), classification)
    }

    def "test selection with sentence only"() {
        given:
        Sentence dummySentence = dummyDocument.getSentences().get(0)
        Classification classification = TestUtils.sampleClassification(dummyModule, dummySentence.getId())
        classificationDao.create(classification)

        when:
        ExternalModule additionalModule = TestUtils.sampleModule()
        additionalModule.setId("additional_testing_module")
        externalModuleDao.create(additionalModule)
        moduleIds.add(additionalModule.getId())

        classification.setExternalModule(additionalModule)
        classificationDao.create(classification)


        then:
        List<Classification> testClassifications = classificationDao.findForSentence(dummySentence)

        for (Classification c : testClassifications) {
            c.getConfidence() == classification.getConfidence()
            c.getValue() == classification.getValue()
            c.getSentenceId() == classification.getSentenceId()
        }
        testClassifications.get(0).getExternalModule().getId() == "additional_testing_module"
        testClassifications.get(1).getExternalModule().getId() == "test_module"

        noExceptionThrown()
    }

    def "test selection with module only"() {
        given:
        List<Sentence> dummySentences = dummyDocument.getSentences()
        Classification classification = TestUtils.sampleClassification(dummyModule, dummySentences.get(0).getId())
        classificationDao.create(classification)

        when:
        ExternalModule additionalModule = TestUtils.sampleModule()
        additionalModule.setId("additional_testing_module")
        externalModuleDao.create(additionalModule)
        moduleIds.add(additionalModule.getId())

        classification.setExternalModule(additionalModule)
        classification.setSentenceId(dummySentences.get(1).getId())
        classificationDao.create(classification)
        classification.setSentenceId(dummySentences.get(2).getId())
        classificationDao.create(classification)


        then:
        List<Classification> testClassifications = classificationDao.findForModule(additionalModule)

        testClassifications.size() == 2
        for (Classification c : testClassifications) {
            c.getConfidence() == classification.getConfidence()
            c.getValue() == classification.getValue()
            c.getSentenceId() == classification.getSentenceId()
        }

        noExceptionThrown()
    }

    def cleanup() {
        TestUtils.cleanupDatabase(jdbcTemplate, sentenceIds, documentIds, moduleIds)
    }

    def compareClassifications(Classification thisClassification, Classification thatClassification) {
        thisClassification.getConfidence() == thatClassification.getConfidence() &&
        thisClassification.getValue() == thatClassification.getValue() &&
        thisClassification.getSentenceId() == thatClassification.getSentenceId() &&
        thisClassification.getExternalModule().getId() == thatClassification.getExternalModule().getId()
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
                Calendar.getInstance(), Calendar.getInstance(), module)
        document.setMetaData(metaData)
        for (int i = 0; i < 3; i++) {
            document.addSentence(TestUtils.sampleSentence())
        }
        return document
    }
}
