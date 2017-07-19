package de.fh_bielefeld.newsboard.model;

import de.smartsquare.ddd.annotations.DDDEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.OptionalDouble;

/**
 * Domain class representing a sentence inside a classifiable document.
 *
 * @author Felix Meyer, Lukas Taake
 */
@DDDEntity
public class Sentence {
    private int id;
    private int number;
    private String text;
    private List<Classification> classifications;

    public Sentence() {
        classifications = new ArrayList<>();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setClassifications(List<Classification> classifications) {
        this.classifications = classifications;
    }

    public void addClassification(Classification classification) {
        classifications.add(classification);
    }

    public List<Classification> getClassifications() {
        return classifications;
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
}
