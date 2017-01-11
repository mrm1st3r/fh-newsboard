package de.fh_bielefeld.newsboard.xml

import de.fh_bielefeld.newsboard.model.Classification
import de.fh_bielefeld.newsboard.model.Document
import de.fh_bielefeld.newsboard.model.DocumentMetaData
import de.fh_bielefeld.newsboard.model.Sentence
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
        List<Document> documents = new ArrayList<>();
        Document doc = createSampleDocument()
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
        when:
        def result = writer.writeDocument(createSampleDocument())

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
                "<classification documentid=\"42\" confidence=\"0.7\">1.0</classification>" +
                "<classification sentenceid=\"24\" confidence=\"0.95\">0.9</classification>" +
                "</classifications></document></documents>"
    }

    private static Document createSampleDocument() {
        DocumentMetaData meta = new DocumentMetaData()
        meta.setAuthor("Hans Wurst")
        meta.setSource("http://example.com")
        meta.setTitle("Wuppi Fluppi")
        meta.setCrawlTime(new GregorianCalendar(2016, Calendar.DECEMBER, 01))
        meta.setCreationTime(new GregorianCalendar(2016, Calendar.NOVEMBER, 30))
        def s1 = new Sentence()
        s1.setId(1)
        s1.setNumber(1)
        s1.setText("Lorem ipsum dolor sit amet.")
        def s2 = new Sentence()
        s2.setId(24)
        s2.setNumber(2)
        s2.setText("Die Würde des Tasters ist unanmenschbar.")
        def c1 = new Classification()
        c1.setConfidence(0.7)
        c1.setDocumentId(42)
        c1.setValue(1)
        def c2 = new Classification();
        c2.setSentenceId(24)
        c2.setConfidence(0.95)
        c2.setValue(0.9)
        s2.addClassification(c2)
        return new Document(42, meta, [s1, s2], [c1]);
    }
}
