package de.fhbielefeld.newsboard.model.access

import de.fhbielefeld.newsboard.model.document.DocumentId
import spock.lang.Specification

class AccessIdTest extends Specification {

    def "should test for equality"() {
        given:
        def id1 = new AccessId("access-a")
        def id2 = new AccessId("access-b")
        def id3 = new AccessId("access-a")
        def id4 = new DocumentId(4)
        def id5 = new AccessId(null)

        expect:
        !id1.equals(null)
        id1 == id1
        id1 != id2
        id2 != id3
        id1 == id3
        id1 != id4
        id1 != id5
        id1.hashCode() == id3.hashCode()
    }

    def "should return raw value"() {
        given:
        def rawId = "access-a"

        when:
        def access = new AccessId(rawId)

        then:
        rawId == access.raw()
    }
}
