package de.fhbielefeld.newsboard.model

import spock.lang.Specification

class SentenceTest extends Specification {

    def "should not add multiple classifications for module"() {
        given:
        ModuleReference classifier = new ModuleReference("test-crawler")
        Sentence sentence = new Sentence(1, 1, "Foo bar.")

        when:
        sentence.addClassification(classifier, 1, OptionalDouble.empty())
        sentence.addClassification(classifier, 0.5, OptionalDouble.empty())

        then:
        thrown(IllegalArgumentException)
        sentence.getClassifications().size() == 1
    }

    def "should calculate average classification value"() {
        given:
        def s = new Sentence(1, 1, "Foo bar.")
        s.addClassification(new ModuleReference("a"), 0.5, OptionalDouble.empty())
        s.addClassification(new ModuleReference("b"), 0.3, OptionalDouble.empty())

        expect:
        s.getAverageClassificationValue() == 0.4d
    }

    def "should calculate average classification with confidence"() {
        given:
        def s = new Sentence(1, 1, "Foo bar.")
        s.addClassification(new ModuleReference("a"), 0.6, OptionalDouble.of(0.5))
        s.addClassification(new ModuleReference("b"), 0.3, OptionalDouble.empty())

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

    def "should not create with number < 1"() {
        when:
        new Sentence(1, 0, "Foo")

        then:
        thrown(IllegalArgumentException)
    }

    def "should not create with empty text"() {
        when:
        new Sentence(1, 1, text)

        then:
        thrown(IllegalArgumentException)

        where:
        text << ["", null]
    }

    def "should not add classification with null module"() {
        given:
        def sentence = new Sentence(1, 1, "Foo")

        when:
        sentence.addClassification(null, 1, OptionalDouble.empty())

        then:
        thrown(IllegalArgumentException)
    }
}
