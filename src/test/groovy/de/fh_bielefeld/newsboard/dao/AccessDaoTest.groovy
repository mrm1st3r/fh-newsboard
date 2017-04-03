package de.fh_bielefeld.newsboard.dao

import de.fh_bielefeld.newsboard.NewsboardApplication
import de.fh_bielefeld.newsboard.TestUtils
import de.fh_bielefeld.newsboard.model.Access
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
        access == accessDao.get(access.getId())
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
        access == accessDao.get(access.getId())
        noExceptionThrown()
    }

    def cleanup() {
        for (String i : accessIds) {
            jdbcTemplate.update("DELETE FROM access WHERE access_id = '" + i + "'")
        }
    }
}
