package de.fh_bielefeld.newsboard.model;

import java.util.List;

/**
 * Model class for tokenized sentences inside a document.
 *
 * @author Lukas Taake
 */
public class Sentence {

    private int number;
    private String text;

    private List<Classification> classifications;

    public Sentence(int number, String text, List<Classification> classifications) {
        this.number = number;
        this.text = text;
        this.classifications = classifications;
    }

    public int getNumber() {
        return number;
    }

    public String getText() {
        return text;
    }

    public List<Classification> getClassifications() {
        return classifications;
    }

    public void addClassification(Classification classification) {
        classifications.add(classification);
    }
}
