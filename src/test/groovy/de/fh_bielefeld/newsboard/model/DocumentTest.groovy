package de.fh_bielefeld.newsboard.model

import spock.lang.Specification

class DocumentTest extends Specification {

    def "should test for equality"() {
        given:
        def doc1 = new Document()
        def doc2 = new Document()
        def doc3 = new Document()
        def sent = new Sentence()
        doc1.setId(42)
        doc2.setId(12)
        sent.setId(42)
        doc3.setId(42)

        expect:
        doc1 != null
        doc1 != doc2
        doc1 != sent
        doc1 == doc1
        doc1 == doc3
    }
}
