package groovy.de.fh_bielefeld.newsboard.opennlp

import de.fh_bielefeld.newsboard.opennlp.DocumentToSentenceTokenizer
import spock.lang.Specification

/**
 * Created by Felix on 17.01.2017.
 */
class DocumentToSentenceTokenizerTest extends Specification {
    String exampleText = "Ich fahre schnell zum Knäckebrot-Laden! " +
            "Der Sprit ist aber auch schon wieder sehr teuer. Fi" +
            "ndest du nicht auch?"

    def "test tokenizing"() {
        when:
        DocumentToSentenceTokenizer tokenizer = new DocumentToSentenceTokenizer("src\\main\\resources\\de-sent.bin")

        then:
        String[] sentences = tokenizer.tokenizePlaintext(exampleText)

        sentences.size() == 3
        sentences[0] == "Ich fahre schnell zum Knäckebrot-Laden!";
        sentences[1] == "Der Sprit ist aber auch schon wieder sehr teuer.";
        sentences[2] == "Findest du nicht auch?";

        noExceptionThrown()
    }
}
