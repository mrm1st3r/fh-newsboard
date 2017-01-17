package de.fh_bielefeld.newsboard.model;

import java.util.OptionalDouble;

/**
 * Domain class representing a classification of a document or sentence.
 *
 * @author Felix Meyer, Lukas Taake
 */
public class Classification {
    private int sentenceId;
    private ExternModule externModule;
    private double value;
    private OptionalDouble confidence = OptionalDouble.empty();

    public int getSentenceId() {
        return sentenceId;
    }

    public void setSentenceId(int sentenceId) {
        this.sentenceId = sentenceId;
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

    public OptionalDouble getConfidence() {
        return confidence;
    }

    public void setConfidence(double confidence) {
        this.confidence = OptionalDouble.of(confidence);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof Classification)) {
            return false;
        }
        Classification that = (Classification) obj;
        return this.sentenceId == that.sentenceId && this.externModule.equals(that.externModule);
    }
}
