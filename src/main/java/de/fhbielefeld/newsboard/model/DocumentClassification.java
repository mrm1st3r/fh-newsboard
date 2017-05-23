package de.fhbielefeld.newsboard.model;

import java.util.List;

/**
 * Classification of a document, done by a specific classifier.
 */
public class DocumentClassification {

    private final ClassificationId id;
    private final ModuleReference module;
    private final List<ClassificationValue> values;

    public DocumentClassification(ClassificationId id, ModuleReference module, List<ClassificationValue> values) {
        this.id = id;
        this.module = module;
        this.values = values;
    }

    public ClassificationId getId() {
        return id;
    }

    public ModuleReference getModule() {
        return module;
    }

    public List<ClassificationValue> getValues() {
        return values;
    }
}
