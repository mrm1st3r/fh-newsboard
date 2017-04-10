package de.fhbielefeld.newsboard.dao

import de.fhbielefeld.newsboard.NewsboardApplication
import de.fhbielefeld.newsboard.TestUtils
import de.fhbielefeld.newsboard.model.*
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
        Document document = TestUtils.sampleDocumentForDb(module)
        accessDao.create(TestUtils.sampleAccess())
        externalModuleDao.create(module)
        moduleIds.add(module.getId())
        insertDocument(document)

        dummyModule = module
        dummyDocument = document
    }

    def "should find for sentence"() {
        given:
        ExternalModule additionalModule = new ExternalModule(
                "additional_testing_module", "", "", "", new AccessReference("test-access"))
        externalModuleDao.create(additionalModule)
        moduleIds.add(additionalModule.getId())
        Sentence dummySentence = dummyDocument.getSentences().get(0)
        Classification c1 = TestUtils.sampleClassification(dummyModule, dummySentence.getId())
        classificationDao.create(c1)

        Classification c2 = TestUtils.sampleClassification(additionalModule, dummySentence.getId())
        classificationDao.create(c2)

        when:
        List<Classification> testClassifications = classificationDao.findForSentence(dummySentence)

        then:
        for (Classification c : testClassifications) {
            c.getConfidence() == c1.getConfidence()
            c.getValue() == c1.getValue()
            c.getSentenceId() == c1.getSentenceId()
        }
        testClassifications.get(0).getExternalModule().getId() == "additional_testing_module"
        testClassifications.get(1).getExternalModule().getId() == "test_module"

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
}
