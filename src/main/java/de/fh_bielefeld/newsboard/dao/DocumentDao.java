package de.fh_bielefeld.newsboard.dao;

import de.fh_bielefeld.newsboard.model.Document;
import de.fh_bielefeld.newsboard.model.DocumentStub;
import de.fh_bielefeld.newsboard.model.ModuleReference;

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
