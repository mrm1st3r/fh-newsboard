package de.fhbielefeld.newsboard.model;

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
    private final int number;
    private final String text;
    private final List<Classification> classifications = new ArrayList<>();

    public Sentence(int id, int number, String text) {
        if (number < 1) {
            throw new IllegalArgumentException("Sentence number must be greater than 0");
        }
        if (text == null || text.length() == 0) {
            throw new IllegalArgumentException("Sentence text must not be empty");
        }
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

    public List<Classification> getClassifications() {
        return new ArrayList<>(classifications);
    }

    public double getAverageClassificationValue() {
        if (classifications.isEmpty()) {
            return 0;
        }
        return classifications.stream().mapToDouble(Classification::weightedValue).sum() / classifications.size();
    }

    public void addClassification(ModuleReference classifier, double value, OptionalDouble confidence) {
        if (classifier == null) {
            throw new IllegalArgumentException("Classifier must not be null");
        }
        if (classifications.stream().anyMatch(c -> c.getExternalModule().equals(classifier))) {
            throw new IllegalArgumentException(String.format(
                    "Sentence with ID %d was already classified by %s", getId(), classifier.getId()));
        }
        classifications.add(new Classification(this.getId(), classifier, value, confidence));
    }
}
