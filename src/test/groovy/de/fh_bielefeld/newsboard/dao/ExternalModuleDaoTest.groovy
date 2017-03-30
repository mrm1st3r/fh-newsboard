package de.fh_bielefeld.newsboard.dao

import de.fh_bielefeld.newsboard.NewsboardApplication
import de.fh_bielefeld.newsboard.TestUtils
import de.fh_bielefeld.newsboard.model.ExternalModule
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.jdbc.core.JdbcTemplate
import spock.lang.Specification

@SpringBootTest(classes = NewsboardApplication.class)
class ExternalModuleDaoTest extends Specification {

    @Autowired
    JdbcTemplate jdbcTemplate
    @Autowired
    ExternalModuleDao externalModuleDao

    def "should insert and select"() {
        given:
        ExternalModule module = TestUtils.sampleModule()

        when:
        externalModuleDao.create(module)

        then:
        compareModules(externalModuleDao.get(module.getId()),module)
    }

    def "test updating"() {
        given:
        ExternalModule module = TestUtils.sampleModule()

        when:
        externalModuleDao.create(module)
        module.setDescription("New Description")
        module.setAuthor("New Author")
        module.setName("New Name")
        externalModuleDao.update(module)

        then:
        compareModules(externalModuleDao.get(module.getId()), module)
    }

    def cleanup() {
        TestUtils.cleanupDatabase(jdbcTemplate, null, null, ['test_module'], null)
    }

    def compareModules(ExternalModule testingModule, ExternalModule module) {
        testingModule != null &&
        testingModule.getAuthor() == module.getAuthor() &&
        testingModule.getId() == module.getId() &&
        testingModule.getDescription() == module.getDescription() &&
        testingModule.getName() == module.getName()
    }
}
