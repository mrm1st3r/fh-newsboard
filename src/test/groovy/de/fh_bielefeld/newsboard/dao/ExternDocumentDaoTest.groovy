package groovy.de.fh_bielefeld.newsboard.dao

import de.fh_bielefeld.newsboard.NewsboardApplication
import de.fh_bielefeld.newsboard.dao.ExternDocumentDao
import de.fh_bielefeld.newsboard.dao.ExternModuleDao
import de.fh_bielefeld.newsboard.model.ExternDocument
import de.fh_bielefeld.newsboard.model.ExternModule
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.jdbc.core.JdbcTemplate
import spock.lang.Specification

/**
 * Created by Felix on 13.01.2017.
 */
@SpringBootTest(classes = NewsboardApplication.class)
class ExternDocumentDaoTest extends Specification {

    @Autowired
    JdbcTemplate jdbcTemplate
    @Autowired
    ExternDocumentDao externDocumentDao
    @Autowired
    ExternModuleDao externModuleDao

    List<String> moduleIds
    List<Integer> documentIds

    ExternModule dummyModule

    def "test insertion"() {
        when:
        ExternDocument document = new ExternDocument()
        document.setHtml("<body><h1>Testing html</h1></body>")
        document.setExternModule(dummyModule)
        document.setTitle("Test extern document")

        then:
        externDocumentDao.insertExternDocument(document);
        documentIds.add(document.getId())

        ExternDocument testDocument = externDocumentDao.getExternDocumentWithId(document.getId())
        testDocument.getId() == document.getId()
        testDocument.getHtml() == document.getHtml()
        testDocument.getTitle() == document.getTitle()
        testDocument.getExternModule().getId() == document.getExternModule().getId()
        testDocument.getExternModule().getAuthor() == document.getExternModule().getAuthor()
        testDocument.getExternModule().getDescription() == document.getExternModule().getDescription()
        testDocument.getExternModule().getName() == document.getExternModule().getName()

        noExceptionThrown()
    }

    def "test updating"() {
        when:
        ExternDocument document = new ExternDocument()
        document.setHtml("<body><h1>Testing html</h1></body>")
        document.setExternModule(dummyModule)
        document.setTitle("Test extern document")
        externDocumentDao.insertExternDocument(document);
        documentIds.add(document.getId())
        document.setHtml("<body><h2>html Testing</h2></body>")
        document.setTitle("Test extern document again")

        then:
        externDocumentDao.updateExternDocument(document)

        ExternDocument testDocument = externDocumentDao.getExternDocumentWithId(document.getId())
        testDocument.getId() == document.getId()
        testDocument.getHtml() == document.getHtml()
        testDocument.getTitle() == document.getTitle()
        testDocument.getExternModule().getId() == document.getExternModule().getId()
        testDocument.getExternModule().getAuthor() == document.getExternModule().getAuthor()
        testDocument.getExternModule().getDescription() == document.getExternModule().getDescription()
        testDocument.getExternModule().getName() == document.getExternModule().getName()

        noExceptionThrown()
    }

    def "test selection with id"() {
        when:
        ExternDocument document = new ExternDocument()
        document.setHtml("<body><h1>Testing html</h1></body>")
        document.setExternModule(dummyModule)
        document.setTitle("Test extern document")
        externDocumentDao.insertExternDocument(document);
        documentIds.add(document.getId())

        then:
        ExternDocument testDocument = externDocumentDao.getExternDocumentWithId(document.getId())
        testDocument.getId() == document.getId()
        testDocument.getHtml() == document.getHtml()
        testDocument.getTitle() == document.getTitle()
        testDocument.getExternModule().getId() == document.getExternModule().getId()
        testDocument.getExternModule().getAuthor() == document.getExternModule().getAuthor()
        testDocument.getExternModule().getDescription() == document.getExternModule().getDescription()
        testDocument.getExternModule().getName() == document.getExternModule().getName()

        noExceptionThrown()
    }

    def setup() {
        documentIds = new ArrayList<>()
        moduleIds = new ArrayList<>()

        ExternModule module = getNewExternModule()
        insertExternModule(module)
        dummyModule = module
    }

    def cleanup() {
        for (Integer id : documentIds) {
            jdbcTemplate.update("DELETE FROM extern_document WHERE id = " + id)
        }
        for (String id : moduleIds) {
            jdbcTemplate.update("DELETE FROM extern_module WHERE id = '" + id + "'")
        }
    }

    def insertExternModule(ExternModule module) {
        externModuleDao.insertExternModule(module)
        moduleIds.add(module.getId())
    }

    def getNewExternModule() {
        ExternModule module = new ExternModule()
        module.setId("test_module")
        module.setAuthor("Tester")
        module.setDescription("Module for testing purpose")
        module.setName("Test module")
        return module
    }
}
