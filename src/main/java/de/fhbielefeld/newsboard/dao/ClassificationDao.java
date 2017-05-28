package de.fhbielefeld.newsboard.dao;

import de.fhbielefeld.newsboard.model.Document;
import de.fhbielefeld.newsboard.model.DocumentClassification;

import java.util.List;

/**
 * Data access interface for classifications.
 */
public interface ClassificationDao {

    int create(DocumentClassification classification);

    List<DocumentClassification> forForDocument(Document document);
}
