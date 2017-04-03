package de.fh_bielefeld.newsboard.model

import spock.lang.Specification

class DocumentTest extends Specification {

    def "should test for equality"() {
        given:
        def doc1 = new Document(42, null, Collections.emptyList())
        def doc2 = new Document(12, null, Collections.emptyList())
        def doc3 = new Document(42, null, Collections.emptyList())
        def sent = new Sentence(42, 1, "Foo bar.")

        expect:
        doc1 != null
        doc1 != doc2
        doc1 != sent
        doc1 == doc1
        doc1 == doc3
    }

    def "should calculate average classification value"() {
        given:
        def s1 = new Sentence(1, 1, "Foo.")
        def s2 = new Sentence(2, 2, "Bar.")
        s1.addClassification(new Classification(1, null, 0.6, OptionalDouble.empty()))
        s1.addClassification(new Classification(1, null, 0.4, OptionalDouble.empty()))
        s2.addClassification(new Classification(2, null, -0.1, OptionalDouble.empty()))
        def d = new Document(1, null, [s1, s2])

        expect:
        d.getAverageClassificationValue() == 0.2d
    }

    def "should calculate average without sentences"() {
        given:
        def d = new Document(1, null, Collections.emptyList())

        expect:
        d.getAverageClassificationValue() == 0
    }

    def "should set ID only once"() {
        given:
        def doc = new Document(-1, null, Collections.emptyList())

        when:
        doc.setId(1)
        doc.setId(2)

        then:
        thrown(IllegalStateException)
        doc.getId() == 1
    }
}
