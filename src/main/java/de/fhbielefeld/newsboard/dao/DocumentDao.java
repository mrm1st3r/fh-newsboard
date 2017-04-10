package de.fhbielefeld.newsboard.dao;

import de.fhbielefeld.newsboard.model.Document;
import de.fhbielefeld.newsboard.model.DocumentStub;
import de.fhbielefeld.newsboard.model.ModuleReference;

import java.util.List;

/**
 * Data access interface for classifiable documents.
 */
public interface DocumentDao {

    Document get(int id);

    int create(Document document);

    /**
     * Persist new classifications of a document aggregate.
     */
    void update(Document document);

    List<DocumentStub> findAllStubs();

    List<Document> findUnclassifiedForModule(ModuleReference module);
}
