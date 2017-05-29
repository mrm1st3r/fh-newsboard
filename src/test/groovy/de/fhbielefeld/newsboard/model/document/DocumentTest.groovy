package de.fhbielefeld.newsboard.model.document

import de.fhbielefeld.newsboard.TestUtils
import spock.lang.Specification

class DocumentTest extends Specification {

    def "should test for equality"() {
        given:
        def doc1 = TestUtils.emptyDocument(42, Collections.emptyList())
        def doc2 = TestUtils.emptyDocument(12, Collections.emptyList())
        def doc3 = TestUtils.emptyDocument(42, Collections.emptyList())
        def sent = new Sentence(42, 1, "Foo bar.")

        expect:
        !doc1.equals(null)
        doc1 != doc2
        doc1 != sent
        doc1 == doc1
        doc1.hashCode() == doc1.hashCode()
        doc1 == doc3
        doc1.hashCode() == doc3.hashCode()
    }

    def "should set ID only once"() {
        given:
        def doc = TestUtils.emptyDocument(-1, Collections.emptyList())

        when:
        doc.setId(1)
        doc.setId(2)

        then:
        thrown(IllegalStateException)
        doc.getId() == 1
    }
}
