package de.fhbielefeld.newsboard.frontend

import de.fhbielefeld.newsboard.model.document.DocumentDao
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.web.servlet.MockMvc
import spock.lang.Specification

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@SpringBootTest
@AutoConfigureMockMvc
class FrontendControllerTest extends Specification {

    @Autowired
    MockMvc mvc

    @Autowired
    DocumentDao documentDao

    def "should access start page"() {
        expect:
        mvc.perform(get("/")).andExpect(status().isOk())
    }

    def "should access detail page"() {
        when:
        def docs = documentDao.findAllStubs()
        int id = docs[0].getId()

        then:
        mvc.perform(get("/document/" + id)).andExpect(status().isOk())
    }
}
