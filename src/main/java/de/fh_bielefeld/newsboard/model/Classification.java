package de.fh_bielefeld.newsboard.model;

/**
 * Domain class representing a classification of a document or sentence.
 *
 * @Author Felix Meyer
 */
public class Classification {
    private Integer sentenceId;
    private Integer documentId;
    private ExternModule externModule;
    private Double value;
    private Double confidence;

    public Integer getSentenceId() {
        return sentenceId;
    }

    public void setSentenceId(Integer sentenceId) {
        this.sentenceId = sentenceId;
    }

    public Integer getDocumentId() {
        return documentId;
    }

    public void setDocumentId(Integer documentId) {
        this.documentId = documentId;
    }

    public ExternModule getExternModule() {
        return externModule;
    }

    public void setExternModule(ExternModule externModule) {
        this.externModule = externModule;
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
