package de.fh_bielefeld.newsboard.dao;

import de.fh_bielefeld.newsboard.model.Document;

/**
 * Created by felixmeyer on 11.12.16.
 */
public interface DocumentDao {
    public Document getDocumentWithId(int id);
}
