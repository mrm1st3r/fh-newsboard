package de.fh_bielefeld.newsboard.model

import spock.lang.Specification

class ClassificationTest extends Specification {

    def "should create correct classifications"() {
        when:
        def c = new Classification(1, new ModuleReference("Test"), value, confidence)

        then:
        c.getConfidence() == confidence
        c.getValue() == value

        where:
        value | confidence
        1d    | OptionalDouble.empty()
        -1d   | OptionalDouble.empty()
        0.5d  | OptionalDouble.of(1)
        -0.7d | OptionalDouble.of(-0.3)
    }

    def "should not accept classification value out of range"() {
        when:
        new Classification(1, new ModuleReference("test-classifier"), value, OptionalDouble.empty())

        then:
        thrown(IllegalArgumentException)

        where:
        value << [-2d, 2d, 7d, 42d, -1.1d, 1.2d]
    }

    def "should not accept confidence out of range"() {
        when:
        new Classification(1, new ModuleReference("test-classifier"), 0.5, OptionalDouble.of(confidence))

        then:
        thrown(IllegalArgumentException)

        where:
        confidence << [2.1d, 1.1d, -1.1d, -7d]
    }

    def "should test for equality"() {
        given:
        def mod1 = new ExternalModule("randomClassifier", "", "", "", new AccessReference(""))
        def mod2 = new ExternalModule("zero-classifier", "", "", "", new AccessReference(""))
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
