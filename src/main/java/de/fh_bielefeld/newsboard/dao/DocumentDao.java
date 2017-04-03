package de.fh_bielefeld.newsboard.dao;

import de.fh_bielefeld.newsboard.model.Document;

import java.util.List;

/**
 * Data access interface for classifiable documents.
 */
public interface DocumentDao {

    Document get(int id);

    int create(Document document);

    List<Document> findAllStubs();

    List<Document> findUnclassifiedForModule(String externalModuleId);
}
