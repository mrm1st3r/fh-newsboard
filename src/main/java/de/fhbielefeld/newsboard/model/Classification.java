package de.fhbielefeld.newsboard.model;

import de.smartsquare.ddd.annotations.DDDEntity;

import java.util.OptionalDouble;

/**
 * Domain class representing a classification of a document or sentence.
 *
 * @author Felix Meyer, Lukas Taake
 */
@DDDEntity
@Deprecated
public class Classification {

    private final int sentenceId;
    private final ModuleReference externalModule;
    private final double value;
    private final OptionalDouble confidence;

    public Classification(int sentenceId, ModuleReference module, double value, OptionalDouble confidence) {
        if (value < -1 || value > 1) {
            throw new IllegalArgumentException("Classification value must be in range [-1 .. 1]");
        }
        if (confidence.isPresent() && (confidence.getAsDouble() < -1 || confidence.getAsDouble() > 1)) {
            throw new IllegalArgumentException("Classification confidence must be in range [-1 .. 1]");
        }
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

    double weightedValue() {
        return confidence.orElse(1) * value;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof Classification)) {
            return false;
        }
        Classification that = (Classification) obj;
        return this.sentenceId == that.sentenceId && this.externalModule.equals(that.externalModule);
    }

    @Override
    public int hashCode() {
        return getSentenceId() + getExternalModule().hashCode();
    }
}
