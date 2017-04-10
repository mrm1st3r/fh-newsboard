package de.fhbielefeld.newsboard.xml

import org.xml.sax.SAXParseException
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

        when:
        reader.readDocument(xml, null)

        then:
        def e = thrown(SAXParseException)
        e.getMessage().contains("sentences")
    }

    def "should read valid document"() {
        given:
        def xml = sampleXml("valid_raw_document")

        when:
        def documents = reader.readDocument(xml, null)

        then:
        documents.size() == 1
        def doc = documents[0]
        doc.getTitle() == "Wuppi Fluppi"
        doc.getAuthor() == "Bla"
        doc.getSource() == "http://wuppi.fluppi"
        doc.getCrawlTime() == new GregorianCalendar(2016, Calendar.DECEMBER, 01)
        doc.getCreationTime() == new GregorianCalendar(2016, Calendar.NOVEMBER, 30)
        documents[0].getRawText().contains("Lorem ipsum")
    }
}
