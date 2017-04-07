package de.fh_bielefeld.newsboard.model;

import java.util.ArrayList;
import java.util.List;
import java.util.OptionalDouble;

/**
 * Domain class representing a sentence inside a classifiable document.
 *
 * @author Felix Meyer, Lukas Taake
 */
public class Sentence {
    private int id;
    private int number;
    private String text;
    private List<Classification> classifications = new ArrayList<>();

    public Sentence(int id, int number, String text) {
        this.id = id;
        this.number = number;
        this.text = text;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        if (this.id != -1) {
            throw new IllegalStateException("Sentence has already an ID assigned");
        }
        this.id = id;
    }

    public int getNumber() {
        return number;
    }

    public String getText() {
        return text;
    }

    public void addClassifications(List<Classification> classifications) {
        this.classifications.addAll(classifications);
    }

    public void addClassification(Classification classification) {
        classifications.add(classification);
    }

    public List<Classification> getClassifications() {
        return new ArrayList<>(classifications);
    }

    public double getAverageClassificationValue() {
        if (classifications.size() == 0) {
            return 0;
        }
        double sum = 0;
        for (Classification c : classifications) {
            OptionalDouble confidence = c.getConfidence();
            if (confidence.isPresent()) {
                sum += c.getValue() * confidence.getAsDouble();
            } else {
                sum += c.getValue();
            }
        }
        return sum / classifications.size();
    }

    public void addClassification(ExternalModule classifier, double value, OptionalDouble confidence) {
        if (classifications.stream().anyMatch(c -> c.getExternalModule().equals(classifier))) {
            throw new IllegalArgumentException("This sentence was already classified by: " + classifier.getId());
        }
        classifications.add(new Classification(this.getId(), classifier, value, confidence));
    }
}
