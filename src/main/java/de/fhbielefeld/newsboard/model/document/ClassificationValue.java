package de.fhbielefeld.newsboard.model.document;

import de.fhbielefeld.newsboard.model.ValueObject;

/**
 * A single classification value consisting of
 * a classification value and an optional confidence.
 */
public class ClassificationValue implements ValueObject {

    private static final double DEFAULT_CONFIDENCE = 1;

    private static final ClassificationValue POSITIVE = new ClassificationValue(1, DEFAULT_CONFIDENCE);
    private static final ClassificationValue NEUTRAL = new ClassificationValue(0, DEFAULT_CONFIDENCE);
    private static final ClassificationValue NEGATIVE = new ClassificationValue(-1, DEFAULT_CONFIDENCE);

    private final double value;
    private final double confidence;

    public static ClassificationValue of(double value) {
        if (Double.compare(value, 0.0) == 0) {
            return NEUTRAL;
        } else if (Double.compare(value, 1.0) == 0) {
            return POSITIVE;
        } else if (Double.compare(value, -1.0) == 0) {
            return NEGATIVE;
        }
        return new ClassificationValue(value, DEFAULT_CONFIDENCE);
    }

    public static ClassificationValue of(double value, double confidence) {
        if (confidence == DEFAULT_CONFIDENCE) {
            return of(value);
        }
        return new ClassificationValue(value, confidence);
    }

    private ClassificationValue(double value, double confidence) {
        if (value < -1 || value > 1) {
            throw new IllegalArgumentException("Classification value must be in range [-1 .. 1]");
        }
        if (confidence < 0 || confidence > 1) {
            throw new IllegalArgumentException("Classification confidence must be in range [0 .. 1]");
        }
        this.value = value;
        this.confidence = confidence;
    }

    public double effectiveValue() {
        return value * confidence;
    }

    public double getValue() {
        return value;
    }

    public double getConfidence() {
        return confidence;
    }
}
