package de.fhbielefeld.newsboard.model.document

import de.fhbielefeld.newsboard.TestUtils
import io.vavr.collection.List
import spock.lang.Specification

class DocumentTest extends Specification {

    def "should test for equality"() {
        given:
        def doc1 = TestUtils.emptyDocument(42, List.of())
        def doc2 = TestUtils.emptyDocument(12, List.of())
        def doc3 = TestUtils.emptyDocument(42, List.of())
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
}
