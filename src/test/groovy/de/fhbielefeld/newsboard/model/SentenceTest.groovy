package de.fhbielefeld.newsboard.model

import spock.lang.Specification

class SentenceTest extends Specification {

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
}
