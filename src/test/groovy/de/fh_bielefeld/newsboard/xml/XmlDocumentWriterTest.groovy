package de.fh_bielefeld.newsboard.xml

import de.fh_bielefeld.newsboard.model.Document
import de.fh_bielefeld.newsboard.model.DocumentMetaData
import spock.lang.Shared
import spock.lang.Specification

class XmlDocumentWriterTest extends Specification {

    @Shared
    private XmlDocumentWriter writer

    def setupSpec() {
        writer = new XmlDocumentWriter()
    }

    def "should write valid document list"() {
        given:
        List<Document> documents = new ArrayList<>();
        DocumentMetaData meta = new DocumentMetaData()
        meta.setAuthor("Hans Wurst")
        meta.setSource("http://example.com")
        meta.setTitle("Wuppi Fluppi")
        meta.setCrawlTime(new GregorianCalendar(2016, Calendar.DECEMBER, 01))
        meta.setCreationTime(new GregorianCalendar(2016, Calendar.NOVEMBER, 30))
        Document doc = new Document(42, meta, null, null);
        documents.add(doc)

        when:
        def result = writer.writeDocumentList(documents)

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
}
