package de.fh_bielefeld.newsboard.xml

import org.xml.sax.SAXParseException
import spock.lang.Shared
import spock.lang.Specification

import static de.fh_bielefeld.newsboard.TestUtils.sampleXml

class XmlDocumentReaderTest extends Specification {

    @Shared
    def XmlDocumentReader reader

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
        def xml = sampleXml("valid_classified_document")

        when:
        def documents = reader.readDocument(xml)

        then:
        documents.size() == 1
        documents[0].getTitle() == "Wuppi Fluppi"
        documents[0].getAuthor() == "Bla"
        documents[0].getSource() == "http://wuppi.fluppi"
        documents[0].getSentences().size() == 2

        def sent = documents[0].getSentences()[0]
        sent.getText() == "Wuppi is very fluppy."
        sent.getId() == 12345

        documents[0].getClassifications().size() == 1
        sent.getClassifications().size() == 1
    }
}
