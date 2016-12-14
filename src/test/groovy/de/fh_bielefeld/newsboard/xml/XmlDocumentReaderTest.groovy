package de.fh_bielefeld.newsboard.xml

import org.xml.sax.SAXParseException
import spock.lang.Shared
import spock.lang.Specification

class XmlDocumentReaderTest extends Specification {

    @Shared
    def XmlDocumentReader reader

    def setupSpec() {
        reader = new XmlDocumentReader()
    }

    def "should not read invalid document"() {
        given:
        def xml = new InputStreamReader(getClass().getResourceAsStream("/invalid_raw_document.xml"))

        when:
        reader.readDocument(xml)

        then:
        def e = thrown(SAXParseException)
        e.getMessage().contains("sentences")
    }

    def "should read valid document"() {
        given:
        def xml = new InputStreamReader(getClass().getResourceAsStream("/valid_classified_document.xml"))

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
    }
}
