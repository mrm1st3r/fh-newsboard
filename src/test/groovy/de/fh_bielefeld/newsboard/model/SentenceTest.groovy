package de.fh_bielefeld.newsboard.model

import spock.lang.Specification

class SentenceTest extends Specification {

    def "should calculate average classification value"() {
        given:
        def s = new Sentence()
        def c1 = new Classification()
        def c2 = new Classification()
        c1.setValue(0.5)
        c2.setValue(0.3)
        s.addClassification(c1)
        s.addClassification(c2)

        expect:
        s.getAverageClassificationValue() == 0.4d
    }

    def "should calculate average classification with confidence"() {
        given:
        def s = new Sentence()
        def c1 = new Classification()
        def c2 = new Classification()
        c1.setValue(0.6)
        c1.setConfidence(0.5)
        c2.setValue(0.3)
        s.addClassification(c1)
        s.addClassification(c2)

        expect:
        s.getAverageClassificationValue() == 0.3d
    }

    def "should calculate average without classifications"() {
        given:
        def s = new Sentence()

        expect:
        s.getAverageClassificationValue() == 0
    }
}
