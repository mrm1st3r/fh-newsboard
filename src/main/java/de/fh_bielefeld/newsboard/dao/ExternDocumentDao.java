package de.fh_bielefeld.newsboard.dao;

import de.fh_bielefeld.newsboard.model.ExternDocument;

import java.util.List;

/**
 * Created by felixmeyer on 11.12.16.
 */
public interface ExternDocumentDao {
    public ExternDocument getExternDocumentWithId(int id);
    public int updateExternDocument(ExternDocument externDocument);
    public int insertExternDocument(ExternDocument externDocument);
}
