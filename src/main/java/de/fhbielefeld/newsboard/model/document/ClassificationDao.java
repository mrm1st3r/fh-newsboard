package de.fhbielefeld.newsboard.model.document;

import java.util.List;

/**
 * Data access interface for classifications.
 */
public interface ClassificationDao {

    int create(DocumentClassification classification);

    List<DocumentClassification> forForDocument(Document document);
}
