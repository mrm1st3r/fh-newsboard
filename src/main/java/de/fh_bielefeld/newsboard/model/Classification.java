package de.fh_bielefeld.newsboard.model;

/**
 * Created by felixmeyer on 11.12.16.
 */
public class Classification {
    private int sentId;
    private int docId;
    private String moduleId;
    private double value;
    private double confidence;

    public Classification(int sentId, int docId, String moduleId, double value, double confidence) {
        this.sentId = sentId;
        this.docId = docId;
        this.moduleId = moduleId;
        this.value = value;
        this.confidence = confidence;
    }

    public int getSentId() {
        return sentId;
    }

    public void setSentId(int sentId) {
        this.sentId = sentId;
    }

    public int getDocId() {
        return docId;
    }

    public void setDocId(int docId) {
        this.docId = docId;
    }

    public String getModuleId() {
        return moduleId;
    }

    public void setModuleId(String moduleId) {
        this.moduleId = moduleId;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public double getConfidence() {
        return confidence;
    }

    public void setConfidence(double confidence) {
        this.confidence = confidence;
    }
}
