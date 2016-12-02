package de.fh_bielefeld.newsboard.rest

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.test.web.servlet.MockMvc
import spock.lang.Specification

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

/**
 * Tests for RestApiController class
 */
@WebMvcTest
class RestApiControllerTest extends Specification {

    @Autowired
    MockMvc mvc

    def "can access document list"() {
        expect:
        mvc.perform(get("/rest/document")).andExpect(status().isOk())
    }
}
