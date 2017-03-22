package de.fh_bielefeld.newsboard.xml

import spock.lang.Shared
import spock.lang.Specification

import static de.fh_bielefeld.newsboard.TestUtils.sampleXml

class ClassificationReadTest extends Specification {

    @Shared
    XmlDocumentReader reader

    def setupSpec() {
        reader = new XmlDocumentReader()
    }

    def "should read valid classifications"() {
        given:
        def xml = sampleXml("valid_new_classifications")

        when:
        def classifications = reader.readClassifications(xml)

        then:
        classifications.size() == 3
        def c = classifications[1]
        c.getConfidence().getAsDouble() == 1
        c.getExternalModule().getId() == "test-classifier"
        c.getSentenceId() == 2
        c.getValue() == -1
    }
}
