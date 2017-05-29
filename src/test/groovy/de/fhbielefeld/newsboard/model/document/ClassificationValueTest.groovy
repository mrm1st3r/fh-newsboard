package de.fhbielefeld.newsboard.model.document

import spock.lang.Specification

class ClassificationValueTest extends Specification {

    def "should use default instances"() {
        when:
        def classification = ClassificationValue.of(value)

        then:
        classification.is(instance)

        where:
        value   | instance
        1       | ClassificationValue.POSITIVE
        0       | ClassificationValue.NEUTRAL
        -1      | ClassificationValue.NEGATIVE
    }

    def "should calculate effectiveValue"() {
        given:
        def classification = ClassificationValue.of(value, confidence)

        expect:
        classification.effectiveValue() == effective

        where:
        value   | confidence    || effective
        1       | 1             || 1
        1       | 0             || 0
        0.5     | 1             || 0.5
        0.5     | 0.5           || 0.25

    }

    def "should not accept classification value out of range"() {
        when:
        ClassificationValue.of(value)

        then:
        thrown(IllegalArgumentException)

        where:
        value << [-2d, 2d, 7d, 42d, -1.1d, 1.2d]
    }

    def "should not accept confidence out of range"() {
        when:
        ClassificationValue.of(0.5,confidence)

        then:
        thrown(IllegalArgumentException)

        where:
        confidence << [2.1d, 1.1d, -0.1d, -7d]
    }


}
