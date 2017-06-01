package de.fhbielefeld.newsboard.dao

import de.fhbielefeld.newsboard.NewsboardApplication
import de.fhbielefeld.newsboard.TestUtils
import de.fhbielefeld.newsboard.model.ExternalDocument
import de.fhbielefeld.newsboard.model.ExternalDocumentDao
import de.fhbielefeld.newsboard.model.access.AccessDao
import de.fhbielefeld.newsboard.model.module.ExternalModule
import de.fhbielefeld.newsboard.model.module.ExternalModuleDao
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.jdbc.core.JdbcTemplate
import spock.lang.Specification

@SpringBootTest(classes = NewsboardApplication.class)
class ExternalDocumentDaoTest extends Specification {

    @Autowired
    JdbcTemplate jdbcTemplate
    @Autowired
    ExternalDocumentDao externalDocumentDao
    @Autowired
    ExternalModuleDao externalModuleDao
    @Autowired
    AccessDao accessDao

    List<String> moduleIds
    List<Integer> documentIds

    ExternalModule dummyModule

    def setup() {
        documentIds = new ArrayList<>()
        moduleIds = new ArrayList<>()

        ExternalModule module = TestUtils.sampleModule()
        accessDao.create(TestUtils.sampleAccess())
        externalModuleDao.create(module)
        moduleIds.add(module.getId().raw())
        dummyModule = module
    }

    def "should insert and select"() {
        when:
        ExternalDocument document = TestUtils.sampleExternalDocument(dummyModule)
        externalDocumentDao.create(document)
        documentIds.add(document.getId())

        then:
        compareDocuments(externalDocumentDao.get(document.getId()), document)
    }

    def "should update correctly"() {
        given:
        ExternalDocument document = TestUtils.sampleExternalDocument(dummyModule)

        when:
        externalDocumentDao.create(document)
        documentIds.add(document.getId())
        document.setHtml("<body><h2>html Testing</h2></body>")
        document.setTitle("Test external document again")
        externalDocumentDao.update(document)

        then:
        compareDocuments(externalDocumentDao.get(document.getId()), document)
    }

    void compareDocuments(ExternalDocument testDocument, ExternalDocument document) {
        assert testDocument.getId() == document.getId()
        assert testDocument.getHtml() == document.getHtml()
        assert testDocument.getTitle() == document.getTitle()
        assert testDocument.getExternalModule().raw() == document.getExternalModule().raw()
    }

    def cleanup() {
        TestUtils.cleanupDatabase(jdbcTemplate, null, null, moduleIds, documentIds)
    }
}
