package de.fhbielefeld.newsboard.xml

import spock.lang.Shared
import spock.lang.Specification

import static de.fhbielefeld.newsboard.TestUtils.sampleXml

class RawDocumentReadTest extends Specification {

    @Shared
    XmlDocumentReader reader

    def setupSpec() {
        reader = new XmlDocumentReader()
    }

    def "should not read invalid document"() {
        given:
        def xml = sampleXml("invalid_raw_document")
        def handler = Mock(DocumentParsedHandler)

        when:
        reader.readDocument(xml, handler)

        then:
        def e = thrown(XmlException)
        e.getMessage().contains("sentences")
    }

    def "should read valid document"() {
        given:
        def xml = sampleXml("valid_raw_document")
        def handler = Mock(DocumentParsedHandler)

        when:
        reader.readDocument(xml, handler)

        then:
        1 * handler.onDocumentParsed("Wuppi Fluppi", "Bla", "http://wuppi.fluppi",
                new GregorianCalendar(2016, Calendar.NOVEMBER, 30),
                new GregorianCalendar(2016, Calendar.DECEMBER, 01),
                {it.startsWith("Lorem ipsum")})
    }
}
