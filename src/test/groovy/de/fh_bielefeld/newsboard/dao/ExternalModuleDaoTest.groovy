package groovy.de.fh_bielefeld.newsboard.dao

import de.fh_bielefeld.newsboard.NewsboardApplication
import de.fh_bielefeld.newsboard.dao.ExternModuleDao
import de.fh_bielefeld.newsboard.model.ExternalModule
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.jdbc.core.JdbcTemplate
import spock.lang.Specification

/**
 * Created by Felix on 13.01.2017.
 */

@SpringBootTest(classes = NewsboardApplication.class)
class ExternalModuleDaoTest extends Specification {

    @Autowired
    JdbcTemplate jdbcTemplate
    @Autowired
    ExternModuleDao externModuleDao

    def "test insertion"() {
        when:
        ExternalModule module = getNewExternModule()

        then:
        externModuleDao.insertExternModule(module)
        ExternalModule testingModule = externModuleDao.getExternModuleWithId(module.getId())

        testingModule != null
        testingModule.getAuthor() == module.getAuthor()
        testingModule.getId() == module.getId()
        testingModule.getDescription() == module.getDescription()
        testingModule.getName() == module.getName()

        noExceptionThrown()
    }

    def "test updating"() {
        when:
        ExternalModule module = getNewExternModule()
        externModuleDao.insertExternModule(module)

        module.setDescription("New Description")
        module.setAuthor("New Author")
        module.setName("New Name")

        then:
        externModuleDao.updateExternModule(module)
        ExternalModule testingModule = externModuleDao.getExternModuleWithId(module.getId())

        testingModule != null
        testingModule.getDescription() == module.getDescription()
        testingModule.getName() == module.getName()
        testingModule.getAuthor() == testingModule.getAuthor()

        noExceptionThrown()
    }

    def "test selecting with id"() {
        when:
        ExternalModule module = getNewExternModule()

        then:
        externModuleDao.insertExternModule(module)
        ExternalModule testingModule = externModuleDao.getExternModuleWithId(module.getId())

        testingModule != null
        testingModule.getDescription() == module.getDescription()
        testingModule.getName() == module.getName()
        testingModule.getAuthor() == testingModule.getAuthor()

        noExceptionThrown()
    }

    def getNewExternModule() {
        ExternalModule externModule = new ExternalModule()
        externModule.setId("test_module")
        externModule.setAuthor("tester")
        externModule.setDescription("Extern module only for testing purpose")
        externModule.setName("Test extern module")
        return externModule
    }

    def cleanup() {
        jdbcTemplate.update("DELETE FROM extern_module WHERE id = 'test_module'")
    }
}
