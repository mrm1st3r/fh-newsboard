package de.fh_bielefeld.newsboard.model;

import java.util.Calendar;
import java.util.List;

/**
 * Model class for News-Documents.
 *
 * @author Lukas Taake
 */
public class Document {

    private int id;
    private String title;
    private String author;
    private String source;
    private Calendar creationTime;
    private Calendar crawlTime;

    private List<Sentence> sentences;
    private List<Classification> classifications;

}
