package de.fhbielefeld.newsboard.dao

import com.google.common.collect.ImmutableList
import de.fhbielefeld.newsboard.NewsboardApplication
import de.fhbielefeld.newsboard.TestUtils
import de.fhbielefeld.newsboard.model.access.AccessDao
import de.fhbielefeld.newsboard.model.access.AccessId
import de.fhbielefeld.newsboard.model.document.*
import de.fhbielefeld.newsboard.model.module.ExternalModule
import de.fhbielefeld.newsboard.model.module.ExternalModuleDao
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.jdbc.core.JdbcTemplate
import spock.lang.Specification

@SpringBootTest(classes = NewsboardApplication)
class ClassificationDaoIntegrationTest extends Specification {

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
        moduleIds.add(module.getId().raw())
        dummyDocument = insertDocument(document)

        dummyModule = module
    }

    def "should find for document"() {
        given:
        ExternalModule additionalModule = new ExternalModule(
                "additional_testing_module", "", "", "", new AccessId("test-access"))
        externalModuleDao.create(additionalModule)
        moduleIds.add(additionalModule.getId().raw())

        DocumentClassification classification = new DocumentClassification(
                dummyDocument.getId(),
                null,
                dummyModule.getId(),
                ImmutableList.copyOf([
                        ClassificationValue.of(1),
                        ClassificationValue.of(-1)
                ]))
        classificationDao.create(classification)

        when:
        List<DocumentClassification> actual = classificationDao.findForDocument(dummyDocument)

        then:
        actual.size() == 1
        def dc = actual.get(0)

        dc.getModule() == dummyModule.getId()
        dc.values.size() == 2
        dc.values[0].value == 1
        dc.values[0].confidence == 1
        dc.values[1].value == -1

        noExceptionThrown()
    }

    def cleanup() {
        TestUtils.cleanupDatabase(jdbcTemplate, sentenceIds, documentIds, moduleIds)
    }

    def insertDocument(Document document) {
        def identified = documentDao.create(document)
        documentIds.add(identified.getId().raw())
        for (Sentence s : document.getSentences()) {
            sentenceIds.add(s.getId())
        }
        return identified
    }
}
