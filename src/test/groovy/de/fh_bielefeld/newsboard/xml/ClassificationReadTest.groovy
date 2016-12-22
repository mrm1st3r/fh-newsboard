package de.fh_bielefeld.newsboard.xml

import spock.lang.Shared
import spock.lang.Specification

import static de.fh_bielefeld.newsboard.TestUtils.sampleXml

class ClassificationReadTest extends Specification {

    @Shared
    def XmlDocumentReader reader

    def setupSpec() {
        reader = new XmlDocumentReader()
    }

    def "should read valid classifications"() {
        given:
        def xml = sampleXml("valid_new_classifications")

        when:
        def classifications = reader.readClassifications(xml)

        then:
        classifications.size() == 4
        def c = classifications[2]
        c.getConfidence() == 1
        c.getExternModule().getId() == "RandomClassifier"
        c.getSentence().getId() == 12346
        c.getValue() == -1
        classifications[3].getDocument().getId() == 123
    }
}