package de.fhbielefeld.newsboard.model.document;

import de.fhbielefeld.newsboard.model.Aggregate;
import de.fhbielefeld.newsboard.model.module.ModuleId;

import java.util.List;

/**
 * Classification of a document, done by a specific classifier.
 */
public class DocumentClassification implements Aggregate<DocumentClassification> {

    private final DocumentId documentId;
    private final ClassificationId id;
    private final ModuleId module;
    private final List<ClassificationValue> values;

    public DocumentClassification(DocumentId documentId, ClassificationId id, ModuleId module, List<ClassificationValue> values) {
        this.documentId = documentId;
        this.id = id;
        this.module = module;
        this.values = values;
    }

    public ClassificationId getId() {
        return id;
    }

    public DocumentId getDocumentId() {
        return documentId;
    }

    public ModuleId getModule() {
        return module;
    }

    public List<ClassificationValue> getValues() {
        return values;
    }
}
