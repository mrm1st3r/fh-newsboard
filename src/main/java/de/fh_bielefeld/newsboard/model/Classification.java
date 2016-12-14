package de.fh_bielefeld.newsboard.model;

/**
 * Created by felixmeyer on 11.12.16.
 */
public class Classification {
    private Sentence sentence;
    private Document document;
    private ExternModule externModule;
    private double value;
    private double confidence;

    public Classification(Sentence sentence, Document document, ExternModule externModule, double value, double confidence) {
        this.sentence = sentence;
        this.document = document;
        this.externModule = externModule;
        this.value = value;
        this.confidence = confidence;
    }

    /**
     * Default constructor needed for SAX-Parsing.
     */
    public Classification() {

    }

    public Sentence getSentence() {
        return sentence;
    }

    public void setSentence(Sentence sentence) {
        this.sentence = sentence;
    }

    public Document getDocument() {
        return document;
    }

    public void setDocument(Document document) {
        this.document = document;
    }

    public ExternModule getExternModule() {
        return externModule;
    }

    public void setExternModule(ExternModule externModule) {
        this.externModule = externModule;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public double getConfidence() {
        return confidence;
    }

    public void setConfidence(double confidence) {
        this.confidence = confidence;
    }
}
