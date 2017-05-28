package de.fhbielefeld.newsboard.model;

import de.smartsquare.ddd.annotations.DDDEntity;

import java.util.*;

/**
 * Domain class representing a classifiable document.
 *
 * @author Felix Meyer, Lukas Taake
 */
@DDDEntity
public class Document extends DocumentStub {
    private List<Sentence> sentences = new ArrayList<>();
    private final List<DocumentClassification> classifications = new ArrayList<>();

    public Document(DocumentStub stub, List<Sentence> sentences) {
        super(stub);
        this.sentences.addAll(sentences);
    }

    public Document(int id, String title, String author, String source, Calendar creationTime, Calendar crawlTime,
                    ModuleReference crawler, List<Sentence> sentences) {
        super(id, title, author, source, creationTime, crawlTime, crawler);
        this.sentences.addAll(sentences);
    }

    public List<Sentence> getSentences() {
        return sentences;
    }

    @Deprecated
    public Sentence getSentenceById(int id) {
        Optional<Sentence> sentence = sentences.stream().filter(s -> s.getId() == id).findFirst();
        if (!sentence.isPresent()) {
            throw new NoSuchElementException("Document doesn't contain a sentence with id: " + id);
        }
        return sentence.get();
    }

    @Deprecated
    public double getAverageClassificationValue() {
        if (sentences.isEmpty()) {
            return 0;
        }
        return sentences.stream().mapToDouble(Sentence::getAverageClassificationValue).sum() / sentences.size();
    }

    public void addClassification(DocumentClassification classification) {
        this.classifications.add(classification);
    }

    public List<DocumentClassification> getClassifications() {
        return classifications;
    }

    public DocumentClassification addClassification(ModuleReference module, List<ClassificationValue> values) {
        return new DocumentClassification(new DocumentId(getId()), null, module, values);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof Document)) {
            return false;
        }
        Document that = (Document) obj;
        return this.getId() == that.getId();
    }

    @Override
    public int hashCode() {
        return getId() + getAuthor().hashCode() + getTitle().hashCode();
    }
}
