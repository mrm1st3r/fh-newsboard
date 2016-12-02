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
}
