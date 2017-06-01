package de.fhbielefeld.newsboard.model;

/**
 * Data access interface for external documents.
 */
public interface ExternalDocumentDao extends Repository {

    ExternalDocument get(int id);

    int update(ExternalDocument externalDocument);

    int create(ExternalDocument externalDocument);
}
