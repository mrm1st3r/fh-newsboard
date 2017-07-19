package de.fh_bielefeld.newsboard.processing;

import de.fh_bielefeld.newsboard.model.Document;
import de.fh_bielefeld.newsboard.model.RawDocument;
import de.fh_bielefeld.newsboard.model.Sentence;
import de.smartsquare.ddd.annotations.DDDService;
import opennlp.tools.sentdetect.SentenceDetectorME;
import opennlp.tools.sentdetect.SentenceModel;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;

/**
 * Tokenizer to split up texts received from crawlers into separate sentences.
 */
@Component
@DDDService
public class RawDocumentProcessor {
    private static final String MODEL_FILE = "/de-sent.bin";
    private SentenceDetectorME sentenceDetector;

    public RawDocumentProcessor() throws IOException {
        try (InputStream modelStream = getClass().getResourceAsStream(MODEL_FILE)) {
            SentenceModel sentenceModel = new SentenceModel(modelStream);
            sentenceDetector = new SentenceDetectorME(sentenceModel);
        }
    }

    public Document processDocument(RawDocument rawDocument) {
        Document document = new Document();
        document.setMetaData(rawDocument.getMetaData());
        int sentNumber = 1;
        for (String s : tokenizePlaintext(rawDocument.getRawText())) {
            Sentence sent = new Sentence();
            sent.setText(s);
            sent.setNumber(sentNumber++);
            document.addSentence(sent);
        }
        return document;
    }

    private String[] tokenizePlaintext(String plaintext) {
        return sentenceDetector.sentDetect(plaintext);
    }
}
