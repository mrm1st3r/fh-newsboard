package de.fhbielefeld.newsboard.model.document;

import de.fhbielefeld.newsboard.model.module.ModuleId;

import java.util.List;

/**
 * Data access interface for classifiable documents.
 */
public interface DocumentDao {

    Document get(DocumentId id);

    Document create(Document document);

    List<Document> findLatest(int maximumAmount);

    List<Document> findUnclassifiedForModule(ModuleId module);
}
