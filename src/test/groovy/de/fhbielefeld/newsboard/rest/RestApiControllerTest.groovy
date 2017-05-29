package de.fhbielefeld.newsboard.rest

import de.fhbielefeld.newsboard.model.document.DocumentDao
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.core.io.ClassPathResource
import org.springframework.jdbc.datasource.init.ScriptUtils
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.ResultActions
import spock.lang.Shared
import spock.lang.Specification

import javax.sql.DataSource

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

/**
 * Tests for RestApiController class
 */
@SpringBootTest
@AutoConfigureMockMvc
class RestApiControllerTest extends Specification {

    @Autowired
    MockMvc mvc

    @Autowired
    DocumentDao documentDao

    @Shared
    boolean initialized

    @Autowired
    void poorMansSetupSpec(DataSource dataSource) {
        if (!initialized) {
            def script = new ClassPathResource("/sql/example_data.sql")
            ScriptUtils.executeSqlScript(dataSource.connection, script)
            initialized = true
        }
    }

    def "can access document list"() {
        expect:
        mvc.perform(get("/rest/document")).andExpect(status().isOk())
    }

    def "can put document"() {
        expect:
        putRequest("/rest/document", "/valid_raw_document.xml", "test-crawler:omglol123").andExpect(status().isOk())
    }

    def "cannot put empty document list"() {
        expect:
        mvc.perform(put("/rest/document").content("<documents xmlns=\"http://fh-bielefeld.de/newsboard\"></documents>"))
                .andExpect(status().is4xxClientError())
    }

    def "can read specific document"() {
        when:
        def docs = documentDao.findAllStubs()
        int id = docs[0].getId()

        then:
        mvc.perform(get("/rest/document/" + id)).andExpect(status().isOk())
    }

    def "can read unclassified documents"() {
       expect:
       mvc.perform(get("/rest/unclassified/test-classifier")).andExpect(status().isOk())
    }

    def "cannot put empty classification list"() {
        expect:
        mvc.perform(put("/rest/classify").content("<documents xmlns=\"http://fh-bielefeld.de/newsboard\"></documents>"))
                .andExpect(status().is4xxClientError())
    }

    def "can put classification list exactly once"() {
        expect:
        putRequest("/rest/classify", "/valid_new_classifications.xml", "test-classifier:abc123").andExpect(status().isOk())
        putRequest("/rest/classify", "/valid_new_classifications.xml", "test-classifier:abc123").andExpect(status().is4xxClientError())
    }

    def "cannot put document without authentication"() {
        putRequest("/rest/document", "/valid_raw_document.xml", "").andExpect(status().isForbidden())
    }

    private ResultActions putRequest(String url, String resourceFile, String creds) {
        def credentials = "Basic " + Base64.getEncoder().encodeToString((creds).getBytes())
        def requestBody = ++(new Scanner(getClass().getResourceAsStream(resourceFile)).useDelimiter("\\A"))
        return mvc.perform(put(url).content(requestBody).contentType("application/xml")
                .accept("application/xml").header("Authorization", credentials))
    }
}
