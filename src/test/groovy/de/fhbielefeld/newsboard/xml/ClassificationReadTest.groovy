package de.fhbielefeld.newsboard.xml

import spock.lang.Shared
import spock.lang.Specification

import static de.fhbielefeld.newsboard.TestUtils.sampleXml

class ClassificationReadTest extends Specification {

    @Shared
    XmlDocumentReader reader

    def setupSpec() {
        reader = new XmlDocumentReader()
    }

    def "should read valid classifications"() {
        given:
        def xml = sampleXml("valid_new_classifications")
        def handler = Mock(ClassificationParsedHandler)

        when:
        reader.readClassifications(xml, handler)

        then:
        1 * handler.onValueParsed(-1, OptionalDouble.empty())
        1 * handler.onValueParsed(-1, OptionalDouble.of(1))
    }
}
