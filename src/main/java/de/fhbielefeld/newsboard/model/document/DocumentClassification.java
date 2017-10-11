package de.fhbielefeld.newsboard.model.document;

import de.fhbielefeld.newsboard.model.Aggregate;
import de.fhbielefeld.newsboard.model.module.ModuleId;
import io.vavr.collection.List;
import lombok.Data;

/**
 * Classification of a document, done by a specific classifier.
 */
@Data
public class DocumentClassification implements Aggregate<DocumentClassification> {

    private final DocumentId documentId;
    private final ClassificationId id;
    private final ModuleId module;
    private final List<ClassificationValue> values;
}
