package de.fhbielefeld.newsboard.dao

import de.fhbielefeld.newsboard.NewsboardApplication
import de.fhbielefeld.newsboard.TestUtils
import de.fhbielefeld.newsboard.model.access.Access
import de.fhbielefeld.newsboard.model.access.AccessDao
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.jdbc.core.JdbcTemplate
import spock.lang.Specification

@SpringBootTest(classes = NewsboardApplication.class)
class AccessDaoTest extends Specification {

    @Autowired
    JdbcTemplate jdbcTemplate
    @Autowired
    AccessDao accessDao

    List<String> accessIds

    def setup() {
        accessIds = new ArrayList<String>()
    }

    def "should insert and select"() {
        given:
        Access access = TestUtils.sampleAccess()

        when:
        accessDao.create(access)
        accessIds.add(access.getId())

        then:
        access == accessDao.get(access)
        noExceptionThrown()
    }

    def "should update correctly"() {
        given:
        Access access = TestUtils.sampleAccess()
        accessDao.create(access)
        accessIds.add(access.getId())

        when:
        access.setPassphrase("ghi")
        accessDao.update(access)

        then:
        access == accessDao.get(access)
        noExceptionThrown()
    }

    def cleanup() {
        for (String i : accessIds) {
            jdbcTemplate.update("DELETE FROM access WHERE access_id = '" + i + "'")
        }
    }
}
