package de.fh_bielefeld.newsboard.model

import spock.lang.Specification

class ClassificationTest extends Specification {

    def "should test for equality"() {
        given:
        def mod1 = new ExternalModule("randomClassifier", "", "", "", "")
        def mod2 = new ExternalModule("zero-classifier", "", "", "", "")
        def c1 = new Classification(1, mod1, 1, OptionalDouble.empty())
        def c2 = new Classification(2, mod1, 1, OptionalDouble.empty())
        def c3 = new Classification(1, mod1, 1, OptionalDouble.empty())
        def c4 = new Classification(1, mod2, 1, OptionalDouble.empty())

        expect:
        c1 != null
        c1 != mod1
        c1 != c2
        c2 != c3
        c1 == c3
        c1 != c4
    }
}
