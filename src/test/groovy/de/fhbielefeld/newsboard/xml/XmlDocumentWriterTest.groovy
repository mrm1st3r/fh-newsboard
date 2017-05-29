package de.fhbielefeld.newsboard.xml

import de.fhbielefeld.newsboard.TestUtils
import de.fhbielefeld.newsboard.model.document.Document
import spock.lang.Shared
import spock.lang.Specification

class XmlDocumentWriterTest extends Specification {

    @Shared
    private XmlDocumentWriter writer

    def setupSpec() {
        writer = new XmlDocumentWriter()
    }

    def "should write valid document stub list"() {
        given:
        List<Document> documents = new ArrayList<>()
        Document doc = TestUtils.sampleDocumentForXml()
        documents.add(doc)

        when:
        def result = writer.writeStubList(documents)

        then:
        result == "<?xml version=\"1.0\" ?><documents xmlns=\"http://fh-bielefeld.de/newsboard\">" +
                "<document id=\"42\"><meta>" +
                "<title>Wuppi Fluppi</title>" +
                "<author>Hans Wurst</author>" +
                "<source>http://example.com</source>" +
                "<creationTime>2016-11-30T00:00:00+01:00</creationTime>" +
                "<crawlTime>2016-12-01T00:00:00+01:00</crawlTime>" +
                "</meta></document>" +
                "</documents>"
    }

    def "should write valid document structure"() {
        given:
        def document = TestUtils.sampleDocumentForXml()
        def classifications = TestUtils.classificationsForDocument(document)

        when:
        def result = writer.writeDocument(document, classifications)

        then:
        result == "<?xml version=\"1.0\" ?><documents xmlns=\"http://fh-bielefeld.de/newsboard\">" +
                "<document id=\"42\"><meta>" +
                "<title>Wuppi Fluppi</title>" +
                "<author>Hans Wurst</author>" +
                "<source>http://example.com</source>" +
                "<creationTime>2016-11-30T00:00:00+01:00</creationTime>" +
                "<crawlTime>2016-12-01T00:00:00+01:00</crawlTime></meta>" +
                "<sentences>" +
                "<sentence id=\"1\">Lorem ipsum dolor sit amet.</sentence>" +
                "<sentence id=\"24\">Die Würde des Tasters ist unanmenschbar.</sentence>" +
                "</sentences><classifications>" +
                "<classification sentence=\"1\" confidence=\"1.0\">1.0</classification>" +
                "<classification sentence=\"2\" confidence=\"0.95\">0.9</classification>" +
                "</classifications></document></documents>"
    }
}
