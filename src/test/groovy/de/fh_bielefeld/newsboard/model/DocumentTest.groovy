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

    def "should calculate average classification value"() {
        given:
        def d = new Document()
        def s1 = new Sentence()
        def s2 = new Sentence()
        def c1 = new Classification()
        def c2 = new Classification()
        def c3 = new Classification()
        c1.setValue(0.6)
        s1.addClassification(c1)
        c2.setValue(0.4)
        s1.addClassification(c2)
        c3.setValue(-0.1)
        s2.addClassification(c3)
        d.addSentence(s1)
        d.addSentence(s2)

        expect:
        d.getAverageClassificationValue() == 0.2d
    }

    def "should calculate average without sentences"() {
        given:
        def d = new Document()

        expect:
        d.getAverageClassificationValue() == 0
    }
}
