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
}
