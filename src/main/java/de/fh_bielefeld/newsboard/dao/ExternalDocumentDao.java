package de.fh_bielefeld.newsboard.dao;

import de.fh_bielefeld.newsboard.model.ExternalDocument;

/**
 * Data access interface for external documents.
 */
public interface ExternalDocumentDao {

    ExternalDocument get(int id);

    int update(ExternalDocument externalDocument);

    int create(ExternalDocument externalDocument);
}
