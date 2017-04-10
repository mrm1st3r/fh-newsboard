package de.fh_bielefeld.newsboard.model;

import java.util.*;

/**
 * Domain class representing a classifiable document.
 *
 * @author Felix Meyer, Lukas Taake
 */
public class Document extends DocumentStub {
    private List<Sentence> sentences = new ArrayList<>();

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

    public Sentence getSentenceById(int id) {
        Optional<Sentence> sentence = sentences.stream().filter(s -> s.getId() == id).findFirst();
        if (!sentence.isPresent()) {
            throw new NoSuchElementException("Document doesn't contain a sentence with id: " + id);
        }
        return sentence.get();
    }

    public double getAverageClassificationValue() {
        if (sentences.isEmpty()) {
            return 0;
        }
        return sentences.stream().mapToDouble(Sentence::getAverageClassificationValue).sum() / sentences.size();
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
