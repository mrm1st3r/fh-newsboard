package de.fhbielefeld.newsboard.model.document;

import com.google.common.collect.ImmutableList;
import de.fhbielefeld.newsboard.model.Aggregate;
import de.fhbielefeld.newsboard.model.module.ModuleId;

import java.util.List;

/**
 * Domain class representing a classifiable document.
 *
 * @author Felix Meyer, Lukas Taake
 */
public class Document implements Aggregate<Document> {

    private final DocumentId id;
    private final DocumentMetaData metaData;
    private final ImmutableList<Sentence> sentences;

    public Document(DocumentMetaData metaData, ImmutableList<Sentence> sentences) {
        id = DocumentId.NONE;
        this.metaData = metaData;
        this.sentences = sentences;
    }

    public Document(DocumentId id, DocumentMetaData metaData, ImmutableList<Sentence> sentences) {
        this.id = id;
        this.metaData = metaData;
        this.sentences = sentences;
    }

    public Document copyForId(int id) {
        return new Document(new DocumentId(id), this.metaData, this.sentences);
    }

    public DocumentId getId() {
        return id;
    }

    public DocumentMetaData getMetaData() {
        return metaData;
    }

    public List<Sentence> getSentences() {
        return sentences;
    }

    public DocumentClassification addClassification(ModuleId module, ImmutableList<ClassificationValue> values) {
        return new DocumentClassification(getId(), null, module, values);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof Document)) {
            return false;
        }
        Document that = (Document) obj;
        return this.getId().equals(that.getId());
    }

    @Override
    public int hashCode() {
        return getId().hashCode();
    }
}
