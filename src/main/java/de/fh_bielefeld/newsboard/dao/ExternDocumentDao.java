package de.fh_bielefeld.newsboard.dao;

import de.fh_bielefeld.newsboard.model.ExternalDocument;

/**
 * Created by felixmeyer on 11.12.16.
 */
public interface ExternDocumentDao {
    public ExternalDocument getExternDocumentWithId(int id);
    public int updateExternDocument(ExternalDocument externalDocument);
    public int insertExternDocument(ExternalDocument externalDocument);
}
