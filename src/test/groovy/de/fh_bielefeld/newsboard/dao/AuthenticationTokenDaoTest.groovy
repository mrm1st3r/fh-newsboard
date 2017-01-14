package groovy.de.fh_bielefeld.newsboard.dao

import de.fh_bielefeld.newsboard.NewsboardApplication
import de.fh_bielefeld.newsboard.dao.AuthenticationTokenDao
import de.fh_bielefeld.newsboard.dao.ExternModuleDao
import de.fh_bielefeld.newsboard.model.AuthenticationToken
import de.fh_bielefeld.newsboard.model.ExternModule
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.jdbc.core.JdbcTemplate
import spock.lang.Specification

/**
 * Created by Felix on 13.01.2017.
 */
@SpringBootTest(classes = NewsboardApplication.class)
class AuthenticationTokenDaoTest extends Specification {

    @Autowired
    JdbcTemplate jdbcTemplate
    @Autowired
    AuthenticationTokenDao authenticationTokenDao
    @Autowired
    ExternModuleDao externModuleDao

    def "test updating"() {
        setup:
        ExternModule additionalExternModule = getNewExternModule()
        additionalExternModule.setId("extern_module_testing_2")
        additionalExternModule.setAuthor("test_author_2")
        additionalExternModule.setName("Additional extern module for testing purpose")
        externModuleDao.insertExternModule(additionalExternModule)

        when:
        AuthenticationToken token = getNewAuthenticationToken()
        authenticationTokenDao.insertAuthenticationToken(token)
        AuthenticationToken insertedToken = getLastInsertedToken()
        insertedToken.setToken("ghi")
        insertedToken.setModuleId("extern_module_testing_2")

        then:
        authenticationTokenDao.updateAuthenticationToken(insertedToken)
        AuthenticationToken testingToken = authenticationTokenDao.getAllTokenForModule(additionalExternModule).get(0)
        testingToken.getToken() == "ghi"
        noExceptionThrown()
    }

    def "test selection with id"() {
        when:
        AuthenticationToken token = getNewAuthenticationToken()
        authenticationTokenDao.insertAuthenticationToken(token)
        AuthenticationToken referenceToken = getLastInsertedToken()

        then:
        AuthenticationToken testingToken = authenticationTokenDao.getTokenWithId(referenceToken.getId())
        testingToken.getModuleId() == referenceToken.getModuleId()
        testingToken.getToken() == referenceToken.getToken()
        noExceptionThrown()
    }

    def "test selection with extern module"() {
        when:
        AuthenticationToken token = getNewAuthenticationToken()
        ExternModule module = getNewExternModule()
        authenticationTokenDao.insertAuthenticationToken(token)
        authenticationTokenDao.insertAuthenticationToken(token)
        authenticationTokenDao.insertAuthenticationToken(token)

        then:
        List<AuthenticationToken> allTokens = authenticationTokenDao.getAllTokenForModule(module)
        allTokens.size() == 3
        for (AuthenticationToken t : allTokens) {
            t.getToken() == token.getToken()
            t.getModuleId() == token.getModuleId()
        }
        noExceptionThrown()
    }

    def "test insertion"() {
        when:
        AuthenticationToken tokenToInsert = getNewAuthenticationToken()
        authenticationTokenDao.insertAuthenticationToken(tokenToInsert)

        then:
        AuthenticationToken token = getLastInsertedToken()
        token.getModuleId() == tokenToInsert.getModuleId()
        token.getToken() == tokenToInsert.getToken()
        noExceptionThrown()
    }

    def setup() {
        externModuleDao.insertExternModule(getNewExternModule())
    }

    def cleanup() {
        jdbcTemplate.update("DELETE FROM extern_module")
        jdbcTemplate.update("DELETE FROM authentication_token")
    }

    def getNewExternModule() {
        ExternModule externModule = new ExternModule()
        externModule.setId("extern_module_test")
        externModule.setAuthor("test_author")
        externModule.setName("Extern_Module for testing purpose")
        return externModule
    }

    def getNewAuthenticationToken() {
        return new AuthenticationToken(-1, "extern_module_test", "abc")
    }

    def getLastInsertedToken() {
        return authenticationTokenDao.getAllTokenForModule(getNewExternModule()).get(0)
    }
}
