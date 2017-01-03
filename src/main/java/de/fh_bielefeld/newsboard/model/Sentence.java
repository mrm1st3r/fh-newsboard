package de.fh_bielefeld.newsboard.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Domain class representing a sentente inside a classifiable document.
 *
 * @author Felix Meyer, Lukas Taake
 */
public class Sentence {
    private int id;
    private int number;
    private String text;
    private List<Classification> classifications;

    public Sentence(int id, int number, String text, ExternModule externModule, List<Classification> classifications) {
        this.id = id;
        this.number = number;
        this.text = text;
        this.classifications = classifications;
    }

    /**
     * Default constructor needed for SAX-Parsing.
     */
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

    public void addClassification(Classification classification) {
        classifications.add(classification);
    }

    public List<Classification> getClassifications() {
        return classifications;
    }
}
