package groovy.de.fh_bielefeld.newsboard.dao.mysql

import de.fh_bielefeld.newsboard.dao.AuthenticationTokenDao
import de.fh_bielefeld.newsboard.dao.mysql.AuthenticationTokenDaoImpl
import de.fh_bielefeld.newsboard.model.AuthenticationToken
import org.apache.tomcat.jdbc.pool.DataSource
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.JdbcTemplate
import spock.lang.Specification

/**
 * Created by felixmeyer on 14.12.16.
 */

class AuthenticationTokenDaoTest extends Specification {
    @Autowired
    AuthenticationTokenDaoImpl authenticationTokenDaoImpl;

    def setup() {
    }

    def cleanup() {

    }

    def "insert AuthenticationToken"() {
        given:
        authenticationTokenDaoImpl.insertAuthenticationToken(new AuthenticationToken(999, "123", "abc"));
        expect:
        AuthenticationToken token = authenticationTokenDaoImpl.getTokenWithId(999);
        token.getId() == 999;
    }

    def "update AuthenticationToken"() {

    }

    def "select all AuthenticationToken for ExternModule"() {

    }

    def "select AuthenticationToken"() {

    }
}