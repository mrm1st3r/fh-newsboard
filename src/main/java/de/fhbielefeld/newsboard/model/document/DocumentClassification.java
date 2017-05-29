package de.fhbielefeld.newsboard.model.document;

import de.fhbielefeld.newsboard.model.module.ModuleReference;

import java.util.List;

/**
 * Classification of a document, done by a specific classifier.
 */
public class DocumentClassification {

    private final DocumentId documentId;
    private final ClassificationId id;
    private final ModuleReference module;
    private final List<ClassificationValue> values;

    public DocumentClassification(DocumentId documentId, ClassificationId id, ModuleReference module, List<ClassificationValue> values) {
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

    public ModuleReference getModule() {
        return module;
    }

    public List<ClassificationValue> getValues() {
        return values;
    }
}
