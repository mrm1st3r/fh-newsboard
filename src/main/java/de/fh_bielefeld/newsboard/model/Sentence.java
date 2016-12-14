package de.fh_bielefeld.newsboard.model;

/**
 * Created by felixmeyer on 11.12.16.
 */
public class Sentence {
    private int id;
    private int number;
    private String text;
    private ExternModule externModule;

    public Sentence(int id, int number, String text, ExternModule externModule) {
        this.id = id;
        this.number = number;
        this.text = text;
        this.externModule = externModule;
    }

    /**
     * Default constructor needed for SAX-Parsing.
     */
    public Sentence() {

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

    public ExternModule getExternModule() {
        return externModule;
    }

    public void setExternModule(ExternModule externModule) {
        this.externModule = externModule;
    }
}
