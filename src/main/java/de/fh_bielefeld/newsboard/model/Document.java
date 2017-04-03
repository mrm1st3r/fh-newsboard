package de.fh_bielefeld.newsboard.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Domain class representing a classifiable document.
 *
 * @author Felix Meyer, Lukas Taake
 */
public class Document extends DocumentStub {
    private List<Sentence> sentences = new ArrayList<>();

    public Document(int id, DocumentMetaData metaData, List<Sentence> sentences) {
        super(id, metaData);
        this.sentences.addAll(sentences);
    }

    public Document(DocumentStub stub, List<Sentence> sentences) {
        super(stub);
        this.sentences.addAll(sentences);
    }

    /**
     * Default constructor needed for SAX-Parsing.
     */
    public Document() {
        super(0, null);
    }

    public List<Sentence> getSentences() {
        return sentences;
    }

    public void setSentences(List<Sentence> sentences) {
        this.sentences = sentences;
    }

    public void addSentence(Sentence sentence) {
        sentences.add(sentence);
    }

    public double getAverageClassificationValue() {
        if (sentences.size() == 0) {
            return 0;
        }
        double sum = 0;
        for (Sentence s : sentences) {
            sum += s.getAverageClassificationValue();
        }
        return sum / sentences.size();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof Document)) {
            return false;
        }
        Document that = (Document) obj;
        return this.getId() == that.getId();
    }
}
