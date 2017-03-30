package de.fh_bielefeld.newsboard.model;

import java.util.OptionalDouble;

/**
 * Domain class representing a classification of a document or sentence.
 *
 * @author Felix Meyer, Lukas Taake
 */
public class Classification {
    private int sentenceId;
    private ExternalModule externalModule;
    private double value;
    private OptionalDouble confidence = OptionalDouble.empty();

    public Classification() {}

    public Classification(int sentenceId, ExternalModule module, double value) {
        this.sentenceId = sentenceId;
        externalModule = module;
        this.value = value;
    }

    public Classification(int sentenceId, ExternalModule module, double value, double confidence) {
        this.sentenceId = sentenceId;
        externalModule = module;
        this.value = value;
        this.confidence = OptionalDouble.of(confidence);
    }

    public int getSentenceId() {
        return sentenceId;
    }

    public void setSentenceId(int sentenceId) {
        this.sentenceId = sentenceId;
    }

    public ExternalModule getExternalModule() {
        return externalModule;
    }

    public void setExternalModule(ExternalModule externalModule) {
        this.externalModule = externalModule;
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
        return this.sentenceId == that.sentenceId && this.externalModule.equals(that.externalModule);
    }
}
