package de.fhbielefeld.newsboard.model

import de.fhbielefeld.newsboard.model.module.ModuleReference
import spock.lang.Specification

class ExternalDocumentTest extends Specification {

    def "should set ID only once"() {
        given:
        def document = new ExternalDocument(-1, "", "", new ModuleReference("test"))

        when:
        document.setId(1)
        document.setId(2)

        then:
        thrown(IllegalStateException)
        document.getId() == 1
    }
}
