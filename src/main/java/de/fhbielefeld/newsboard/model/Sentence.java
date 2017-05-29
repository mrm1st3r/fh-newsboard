package de.fhbielefeld.newsboard.model;

/**
 * Domain class representing a sentence inside a classifiable document.
 *
 * @author Felix Meyer, Lukas Taake
 */
public class Sentence {
    private int id;
    private final int number;
    private final String text;

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
}
