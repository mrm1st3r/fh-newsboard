package de.fhbielefeld.newsboard.dao

import de.fhbielefeld.newsboard.NewsboardApplication
import de.fhbielefeld.newsboard.TestUtils
import de.fhbielefeld.newsboard.model.access.AccessDao
import de.fhbielefeld.newsboard.model.module.ExternalModule
import de.fhbielefeld.newsboard.model.module.ExternalModuleDao
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
    @Autowired
    AccessDao accessDao

    def setup() {
        accessDao.create(TestUtils.sampleAccess())
    }

    def "should insert and select"() {
        given:
        ExternalModule module = TestUtils.sampleModule()

        when:
        externalModuleDao.create(module)

        then:
        compareModules(externalModuleDao.get(module),module)
    }

    def "should update correctly"() {
        given:
        ExternalModule module = TestUtils.sampleModule()

        when:
        externalModuleDao.create(module)
        module.setDescription("New Description")
        module.setAuthor("New Author")
        module.setName("New Name")
        externalModuleDao.update(module)

        then:
        compareModules(externalModuleDao.get(module), module)
    }

    def cleanup() {
        TestUtils.cleanupDatabase(jdbcTemplate, null, null, [TestUtils.sampleModule().getId()], null)
    }

    def compareModules(ExternalModule testingModule, ExternalModule module) {
        testingModule != null &&
        testingModule.getAuthor() == module.getAuthor() &&
        testingModule.getId() == module.getId() &&
        testingModule.getDescription() == module.getDescription() &&
        testingModule.getName() == module.getName()
    }
}
