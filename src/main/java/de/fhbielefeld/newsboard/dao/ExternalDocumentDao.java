package de.fhbielefeld.newsboard.dao;

import de.fhbielefeld.newsboard.model.ExternalDocument;

/**
 * Data access interface for external documents.
 */
public interface ExternalDocumentDao {

    ExternalDocument get(int id);

    int update(ExternalDocument externalDocument);

    int create(ExternalDocument externalDocument);
}
