package de.fh_bielefeld.newsboard.rest

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.ResultActions
import spock.lang.Specification

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

    def "can access document list"() {
        expect:
        mvc.perform(get("/rest/document")).andExpect(status().isOk())
    }

    def "can put document"() {
        expect:
        putRequest("/rest/document", "/valid_raw_document.xml").andExpect(status().isOk())
    }

    def "cannot put empty document list"() {
        expect:
        mvc.perform(put("/rest/document").content("")).andExpect(status().is4xxClientError())
    }

    private ResultActions putRequest(String url, String resourceFile) {
        def requestBody = ++(new Scanner(getClass().getResourceAsStream(resourceFile)).useDelimiter("\\A"))
        return mvc.perform(put(url).content(requestBody).contentType("application/xml").accept("application/xml"))
    }
}
