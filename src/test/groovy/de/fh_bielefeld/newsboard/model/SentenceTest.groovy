package de.fh_bielefeld.newsboard.model

import spock.lang.Specification

class SentenceTest extends Specification {

    def "should calculate average classification value"() {
        given:
        def s = new Sentence(1, 1, "Foo bar.")
        s.addClassification(new Classification(1, null, 0.5, OptionalDouble.empty()))
        s.addClassification(new Classification(1, null, 0.3, OptionalDouble.empty()))

        expect:
        s.getAverageClassificationValue() == 0.4d
    }

    def "should calculate average classification with confidence"() {
        given:
        def s = new Sentence(1, 1, "Foo bar.")
        s.addClassification(new Classification(1, null, 0.6, OptionalDouble.of(0.5)))
        s.addClassification(new Classification(1, null, 0.3, OptionalDouble.empty()))

        expect:
        s.getAverageClassificationValue() == 0.3d
    }

    def "should calculate average without classifications"() {
        given:
        def s = new Sentence(1, 1, "Foo bar.")

        expect:
        s.getAverageClassificationValue() == 0
    }

    def "should set ID only once"() {
        given:
        Sentence sent = new Sentence(-1, 1, "Foo bar.")

        when:
        sent.setId(1)
        sent.setId(2)

        then:
        thrown(IllegalStateException)
        sent.getId() == 1
    }
}
