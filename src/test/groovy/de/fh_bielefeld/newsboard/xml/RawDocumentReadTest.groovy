package de.fh_bielefeld.newsboard.xml

import org.xml.sax.SAXParseException
import spock.lang.Shared
import spock.lang.Specification

import static de.fh_bielefeld.newsboard.TestUtils.sampleXml

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
        reader.readDocument(xml)

        then:
        def e = thrown(SAXParseException)
        e.getMessage().contains("sentences")
    }

    def "should read valid document"() {
        given:
        def xml = sampleXml("valid_raw_document")

        when:
        def documents = reader.readDocument(xml)

        then:
        documents.size() == 1
        def meta = documents[0].getMetaData()
        meta.getTitle() == "Wuppi Fluppi"
        meta.getAuthor() == "Bla"
        meta.getSource() == "http://wuppi.fluppi"
        meta.getCrawlTime() == new GregorianCalendar(2016, Calendar.DECEMBER, 01)
        meta.getCreationTime() == new GregorianCalendar(2016, Calendar.NOVEMBER, 30)
        documents[0].getRawText().contains("Lorem ipsum")
    }
}
