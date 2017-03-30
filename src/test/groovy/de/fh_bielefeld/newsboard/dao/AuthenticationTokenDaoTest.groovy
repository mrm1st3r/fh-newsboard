package de.fh_bielefeld.newsboard.dao

import de.fh_bielefeld.newsboard.NewsboardApplication
import de.fh_bielefeld.newsboard.TestUtils
import de.fh_bielefeld.newsboard.model.AuthenticationToken
import de.fh_bielefeld.newsboard.model.ExternalModule
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.jdbc.core.JdbcTemplate
import spock.lang.Ignore
import spock.lang.Specification

@Ignore
@SpringBootTest(classes = NewsboardApplication.class)
class AuthenticationTokenDaoTest extends Specification {

    @Autowired
    JdbcTemplate jdbcTemplate
    @Autowired
    AuthenticationTokenDao authenticationTokenDao
    @Autowired
    ExternalModuleDao externModuleDao

    List<String> moduleIds
    List<Integer> authTokenIds

    def "test updating"() {
        given:
        ExternalModule additionalExternModule = new ExternalModule(
        "extern_module_testing_2", "tester", "test_author_2", null)
        externModuleDao.create(additionalExternModule)
        moduleIds.add(additionalExternModule.getId())

        when:
        AuthenticationToken token = getNewAuthenticationToken()
        insertToken(token)
        token.setToken("ghi")
        token.setModuleId("extern_module_testing_2")
        authenticationTokenDao.update(token)

        then:
        AuthenticationToken testingToken = authenticationTokenDao.get(token.getId())
        testingToken.getToken() == token.getToken()
        testingToken.getId() == token.getId()
        testingToken.getModuleId() == token.getModuleId()

        noExceptionThrown()
    }

    def "test selection with id"() {
        when:
        AuthenticationToken token = getNewAuthenticationToken()
        insertToken(token)

        then:
        AuthenticationToken testingToken = authenticationTokenDao.get(token.getId())

        testingToken.getModuleId() == token.getModuleId()
        testingToken.getToken() == token.getToken()
        testingToken.getId() == token.getId()

        noExceptionThrown()
    }

    def "test selection with extern module"() {
        when:
        AuthenticationToken token = getNewAuthenticationToken()
        ExternalModule module = TestUtils.sampleModule()
        insertToken(token)
        insertToken(token)
        insertToken(token)
        insertToken(token)
        insertToken(token)

        then:
        List<AuthenticationToken> allTokens = authenticationTokenDao.findForModule(module)

        allTokens.size() == 5
        for (AuthenticationToken t : allTokens) {
            t.getToken() == token.getToken()
            t.getModuleId() == token.getModuleId()
            t.getId() == token.getId()
        }

        noExceptionThrown()
    }

    def "test insertion"() {
        when:
        AuthenticationToken tokenToInsert = getNewAuthenticationToken()
        insertToken(tokenToInsert)

        then:
        AuthenticationToken token = authenticationTokenDao.get(tokenToInsert.getId())

        token.getModuleId() == tokenToInsert.getModuleId()
        token.getToken() == tokenToInsert.getToken()

        noExceptionThrown()
    }

    def setup() {
        moduleIds = new ArrayList<String>()
        authTokenIds = new ArrayList<Integer>()

        ExternalModule module = TestUtils.sampleModule()
        externModuleDao.create(module)
        moduleIds.add(module.getId())
    }

    def cleanup() {
        for (Integer i : authTokenIds) {
            jdbcTemplate.update("DELETE FROM authentication_token WHERE id = " + i)
        }
        for (String s : moduleIds) {
            jdbcTemplate.update("DELETE FROM extern_module WHERE id = '" + s + "'")
        }
    }

    def insertToken(AuthenticationToken token) {
        authenticationTokenDao.create(token)
        authTokenIds.add(token.getId())
    }

    def getNewAuthenticationToken() {
        return new AuthenticationToken(-1, "extern_module_test", "abc")
    }
}
