package de.fhbielefeld.newsboard.model.document;

import de.fhbielefeld.newsboard.model.Service;
import io.vavr.collection.Iterator;
import io.vavr.collection.List;
import opennlp.tools.sentdetect.SentenceDetectorME;
import opennlp.tools.sentdetect.SentenceModel;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;

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
        List<String> sentenceStrings = List.of(tokenizePlaintext(rawDocument.getRawText()));
        List<Sentence> sentences = Iterator.range(0, sentenceStrings.size())
                .map(num -> new Sentence(-1, num + 1, sentenceStrings.get(num)))
                .toList();
        return new Document(rawDocument.getMetaData(), sentences);
    }

    private String[] tokenizePlaintext(String plaintext) {
        return sentenceDetector.sentDetect(plaintext);
    }
}
