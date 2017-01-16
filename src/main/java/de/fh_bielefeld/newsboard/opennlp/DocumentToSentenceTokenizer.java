package de.fh_bielefeld.newsboard.opennlp;

import opennlp.tools.sentdetect.SentenceDetectorME;
import opennlp.tools.sentdetect.SentenceModel;
import org.springframework.stereotype.Component;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Felix on 17.01.2017.
 */
@Component
public class DocumentToSentenceTokenizer {
    private SentenceDetectorME sentenceDetector;

    public DocumentToSentenceTokenizer(String path) {
        SentenceModel sentenceModel = null;
        try (InputStream modelStream = new FileInputStream(path)) {
            sentenceModel = new SentenceModel(modelStream);
            sentenceDetector = new SentenceDetectorME(sentenceModel);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String[] tokenizePlaintext(String plaintext) {
        return sentenceDetector.sentDetect(plaintext);
    }
}
