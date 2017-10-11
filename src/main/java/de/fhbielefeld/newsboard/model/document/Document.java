package de.fhbielefeld.newsboard.model.document;

import de.fhbielefeld.newsboard.model.Aggregate;
import de.fhbielefeld.newsboard.model.module.ModuleId;
import io.vavr.collection.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Domain class representing a classifiable document.
 *
 * @author Felix Meyer, Lukas Taake
 */
@Data
@AllArgsConstructor
@EqualsAndHashCode(of = {"id"})
public class Document implements Aggregate<Document> {

    private final DocumentId id;
    private final DocumentMetaData metaData;
    private final List<Sentence> sentences;

    public Document(DocumentMetaData metaData, List<Sentence> sentences) {
        id = DocumentId.NONE;
        this.metaData = metaData;
        this.sentences = sentences;
    }

    public Document copyForId(int id) {
        return new Document(new DocumentId(id), this.metaData, this.sentences);
    }

    public DocumentClassification addClassification(ModuleId module, List<ClassificationValue> values) {
        return new DocumentClassification(getId(), null, module, values);
    }
}
