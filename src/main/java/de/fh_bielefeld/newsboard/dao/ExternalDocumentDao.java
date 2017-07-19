package de.fh_bielefeld.newsboard.dao;

import de.fh_bielefeld.newsboard.model.ExternalDocument;
import de.smartsquare.ddd.annotations.DDDRepository;

/**
 * Data access interface for external documents.
 */
@DDDRepository
public interface ExternalDocumentDao {

    ExternalDocument get(int id);

    int update(ExternalDocument externalDocument);

    int create(ExternalDocument externalDocument);
}
