package de.fhbielefeld.newsboard.model.document;

import de.fhbielefeld.newsboard.model.Service;
import opennlp.tools.sentdetect.SentenceDetectorME;
import opennlp.tools.sentdetect.SentenceModel;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

/**
 * Tokenizer to split up texts received from crawlers into separate sentences.
 */
@Component
public class RawDocumentProcessor implements Service {

    private static final String MODEL_FILE = "/de-sent.bin";

    private final SentenceDetectorME sentenceDetector;

    public RawDocumentProcessor() throws IOException {
        try (InputStream modelStream = getClass().getResourceAsStream(MODEL_FILE)) {
            SentenceModel sentenceModel = new SentenceModel(modelStream);
            sentenceDetector = new SentenceDetectorME(sentenceModel);
        }
    }

    public Document processDocument(RawDocument rawDocument) {
        int sentNumber = 1;
        ArrayList<Sentence> sentences = new ArrayList<>();
        for (String s : tokenizePlaintext(rawDocument.getRawText())) {
            sentences.add(new Sentence(-1, sentNumber++, s));
        }
        return new Document(rawDocument.getMetaData(), sentences);
    }

    private String[] tokenizePlaintext(String plaintext) {
        return sentenceDetector.sentDetect(plaintext);
    }
}
