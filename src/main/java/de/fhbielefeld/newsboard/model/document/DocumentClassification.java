package de.fhbielefeld.newsboard.model.document;

import com.google.common.collect.ImmutableList;
import de.fhbielefeld.newsboard.model.Aggregate;
import de.fhbielefeld.newsboard.model.module.ModuleId;
import lombok.Data;

/**
 * Classification of a document, done by a specific classifier.
 */
@Data
public class DocumentClassification implements Aggregate<DocumentClassification> {

    private final DocumentId documentId;
    private final ClassificationId id;
    private final ModuleId module;
    private final ImmutableList<ClassificationValue> values;
}
