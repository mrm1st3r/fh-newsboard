package de.fh_bielefeld.newsboard.model;

/**
 * Model class for classifications related to a sentence or whole document.
 *
 * @author Lukas Taake
 */
public class Classification {

    private double value;
    private double confidence;
    private Classifier classifier;

    public Classification(double value, double confidence, Classifier classifier) {
        this.value = value;
        this.confidence = confidence;
        this.classifier = classifier;
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

    public Classifier getClassifier() {
        return classifier;
    }
}
