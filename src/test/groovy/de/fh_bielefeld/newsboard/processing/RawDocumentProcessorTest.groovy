package de.fh_bielefeld.newsboard.processing

import de.fh_bielefeld.newsboard.model.RawDocument
import spock.lang.Specification

class RawDocumentProcessorTest extends Specification {

    String exampleText = "Ich fahre schnell zum Knäckebrot-Laden! " +
            "Der Sprit ist aber auch schon wieder sehr teuer. Fi" +
            "ndest du nicht auch?"

    private RawDocumentProcessor tokenizer = new RawDocumentProcessor()

    def "should split up sentences"() {
        when:
        String[] sentences = tokenizer.tokenizePlaintext(exampleText)

        then:
        sentences.size() == 3
        sentences[0] == "Ich fahre schnell zum Knäckebrot-Laden!"
        sentences[1] == "Der Sprit ist aber auch schon wieder sehr teuer."
        sentences[2] == "Findest du nicht auch?"

        noExceptionThrown()
    }

    def "should create document from raw"() {
        given:
        def raw = new RawDocument("Die Entdeckung des Nichts", "Hans Wurst",
                null, null, null, null, exampleText)

        when:
        def doc = tokenizer.processDocument(raw)

        then:
        doc.getSentences().size() == 3
        doc.getTitle() == "Die Entdeckung des Nichts"
        def sents = doc.getSentences()
        sents[0].getText() == "Ich fahre schnell zum Knäckebrot-Laden!"
        sents[0].getNumber() == 1
        sents[2].getNumber() == 3
    }
}
