package de.fh_bielefeld.newsboard.model;

import java.util.OptionalDouble;
import java.util.OptionalInt;

/**
 * Domain class representing a classification of a document or sentence.
 *
 * @author Felix Meyer, Lukas Taake
 */
public class Classification {
    private OptionalInt sentenceId = OptionalInt.empty();
    private ExternModule externModule;
    private double value;
    private OptionalDouble confidence = OptionalDouble.empty();

    public OptionalInt getSentenceId() {
        return sentenceId;
    }

    public void setSentenceId(int sentenceId) {
        this.sentenceId = OptionalInt.of(sentenceId);
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
        return this.sentenceId.equals(that.sentenceId)
                && this.externModule.equals(that.externModule);
    }
}
