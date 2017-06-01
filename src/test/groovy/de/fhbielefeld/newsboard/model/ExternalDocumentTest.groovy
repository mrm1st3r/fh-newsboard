package de.fhbielefeld.newsboard.model

import de.fhbielefeld.newsboard.model.module.ModuleId
import spock.lang.Specification

class ExternalDocumentTest extends Specification {

    def "should set ID only once"() {
        given:
        def document = new ExternalDocument(-1, "", "", new ModuleId("test"))

        when:
        document.setId(1)
        document.setId(2)

        then:
        thrown(IllegalStateException)
        document.getId() == 1
    }
}
