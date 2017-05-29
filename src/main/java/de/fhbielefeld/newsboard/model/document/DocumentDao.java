package de.fhbielefeld.newsboard.model.document;

import de.fhbielefeld.newsboard.model.module.ModuleReference;

import java.util.List;

/**
 * Data access interface for classifiable documents.
 */
public interface DocumentDao {

    Document get(int id);

    int create(Document document);

    List<DocumentStub> findAllStubs();

    List<Document> findUnclassifiedForModule(ModuleReference module);
}
