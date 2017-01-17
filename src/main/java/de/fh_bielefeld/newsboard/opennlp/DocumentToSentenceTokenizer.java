package de.fh_bielefeld.newsboard.opennlp;

import opennlp.tools.sentdetect.SentenceDetectorME;
import opennlp.tools.sentdetect.SentenceModel;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;

/**
 * Tokenizer to split up texts received from crawlers into separate sentences.
 */
@Component
public class DocumentToSentenceTokenizer {
    private static final String MODEL_FILE = "/de-sent.bin";
    private SentenceDetectorME sentenceDetector;

    public DocumentToSentenceTokenizer() throws IOException {
        SentenceModel sentenceModel;
        try (InputStream modelStream = getClass().getResourceAsStream(MODEL_FILE)) {
            sentenceModel = new SentenceModel(modelStream);
            sentenceDetector = new SentenceDetectorME(sentenceModel);
        } catch (IOException e) {
            throw e;
        }
    }

    public String[] tokenizePlaintext(String plaintext) {
        return sentenceDetector.sentDetect(plaintext);
    }
}
