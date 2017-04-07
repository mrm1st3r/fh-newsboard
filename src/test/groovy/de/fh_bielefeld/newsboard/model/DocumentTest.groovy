package de.fh_bielefeld.newsboard.model

import de.fh_bielefeld.newsboard.TestUtils
import spock.lang.Specification

class DocumentTest extends Specification {

    def "should test for equality"() {
        given:
        def doc1 = TestUtils.emptyDocument(42, Collections.emptyList())
        def doc2 = TestUtils.emptyDocument(12, Collections.emptyList())
        def doc3 = TestUtils.emptyDocument(42, Collections.emptyList())
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
        s1.addClassification(new ModuleReference("a"), 0.6, OptionalDouble.empty())
        s1.addClassification(new ModuleReference("b"), 0.4, OptionalDouble.empty())
        s2.addClassification(new ModuleReference("a"), -0.1, OptionalDouble.empty())
        def d = TestUtils.emptyDocument(1, [s1, s2])

        expect:
        d.getAverageClassificationValue() == 0.2d
    }

    def "should calculate average without sentences"() {
        given:
        def d = TestUtils.emptyDocument(42, Collections.emptyList())

        expect:
        d.getAverageClassificationValue() == 0
    }

    def "should set ID only once"() {
        given:
        def doc = TestUtils.emptyDocument(-1, Collections.emptyList())

        when:
        doc.setId(1)
        doc.setId(2)

        then:
        thrown(IllegalStateException)
        doc.getId() == 1
    }

    def "should not find unknown sentence"() {
        given:
        def sentence = new Sentence(1, 1, "Foo")
        def document = new Document(1, "", "", "", null, null, null, [sentence])

        when:
        document.getSentenceById(2)

        then:
        thrown(NoSuchElementException)
    }
}
