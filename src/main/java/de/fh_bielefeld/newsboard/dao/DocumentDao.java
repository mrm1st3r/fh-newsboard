package de.fh_bielefeld.newsboard.dao;

import de.fh_bielefeld.newsboard.model.Document;

import java.util.List;

/**
 * Created by felixmeyer on 11.12.16.
 */
public interface DocumentDao {
    public Document getDocumentWithId(int id);
    public List<Document> getAllDocumentsOnlyWithMetaData();
    public int updateDocument(Document document);
    public int insertDocument(Document document);
}
