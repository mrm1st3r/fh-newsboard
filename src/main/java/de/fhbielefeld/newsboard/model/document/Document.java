package de.fhbielefeld.newsboard.model.document;

import de.fhbielefeld.newsboard.model.Aggregate;
import de.fhbielefeld.newsboard.model.module.ModuleReference;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Domain class representing a classifiable document.
 *
 * @author Felix Meyer, Lukas Taake
 */
public class Document extends DocumentStub implements Aggregate<Document> {
    private final List<Sentence> sentences = new ArrayList<>();

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
