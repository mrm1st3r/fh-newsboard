package de.fh_bielefeld.newsboard.model;

import java.util.OptionalDouble;

/**
 * Domain class representing a classification of a document or sentence.
 *
 * @author Felix Meyer, Lukas Taake
 */
public class Classification {
    private final int sentenceId;
    private ModuleReference externalModule;
    private final double value;
    private final OptionalDouble confidence;

    public Classification(int sentenceId, ModuleReference module, double value, OptionalDouble confidence) {
        this.sentenceId = sentenceId;
        externalModule = module;
        this.value = value;
        this.confidence = confidence;
    }

    public int getSentenceId() {
        return sentenceId;
    }

    public ModuleReference getExternalModule() {
        return externalModule;
    }

    public double getValue() {
        return value;
    }

    public OptionalDouble getConfidence() {
        return confidence;
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
