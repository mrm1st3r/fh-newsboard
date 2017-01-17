package de.fh_bielefeld.newsboard.opennlp

import spock.lang.Specification

class DocumentToSentenceTokenizerTest extends Specification {
    String exampleText = "Ich fahre schnell zum Knäckebrot-Laden! " +
            "Der Sprit ist aber auch schon wieder sehr teuer. Fi" +
            "ndest du nicht auch?"

    def "test tokenizing"() {
        when:
        DocumentToSentenceTokenizer tokenizer = new DocumentToSentenceTokenizer()

        then:
        String[] sentences = tokenizer.tokenizePlaintext(exampleText)

        sentences.size() == 3
        sentences[0] == "Ich fahre schnell zum Knäckebrot-Laden!";
        sentences[1] == "Der Sprit ist aber auch schon wieder sehr teuer.";
        sentences[2] == "Findest du nicht auch?";

        noExceptionThrown()
    }
}
