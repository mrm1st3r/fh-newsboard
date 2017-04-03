package de.fh_bielefeld.newsboard.dao

import de.fh_bielefeld.newsboard.NewsboardApplication
import de.fh_bielefeld.newsboard.TestUtils
import de.fh_bielefeld.newsboard.model.ExternalDocument
import de.fh_bielefeld.newsboard.model.ExternalModule
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
        moduleIds.add(module.getId())
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

    def compareDocuments(ExternalDocument testDocument, ExternalDocument document) {
        testDocument.getId() == document.getId() &&
        testDocument.getHtml() == document.getHtml() &&
        testDocument.getTitle() == document.getTitle() &&
        testDocument.getExternalModule().getId() == document.getExternalModule().getId() &&
        testDocument.getExternalModule().getAuthor() == document.getExternalModule().getAuthor() &&
        testDocument.getExternalModule().getDescription() == document.getExternalModule().getDescription() &&
        testDocument.getExternalModule().getName() == document.getExternalModule().getName()
    }

    def cleanup() {
        TestUtils.cleanupDatabase(jdbcTemplate, null, null, moduleIds, documentIds)
    }
}
