package de.fhbielefeld.newsboard.model.document;

import de.fhbielefeld.newsboard.model.Entity;
import lombok.Getter;

/**
 * Domain class representing a sentence inside a classifiable document.
 *
 * @author Felix Meyer, Lukas Taake
 */
@Getter
public class Sentence implements Entity<Document> {
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

    public void setId(int id) {
        if (this.id != -1) {
            throw new IllegalStateException("Sentence has already an ID assigned");
        }
        this.id = id;
    }
}
