package de.fh_bielefeld.newsboard.dao;

import de.fh_bielefeld.newsboard.model.Document;

import java.util.List;

/**
 * Data access interface for classifiable documents.
 */
public interface DocumentDao {
    Document getDocumentWithId(int id);
    List<Document> getAllDocumentsOnlyWithMetaData();
    List<Document> getUnclassifiedDocumentStubs(String externalModuleId);
    int updateDocument(Document document);
    int insertDocument(Document document);
    int insertDocumentWithSentences(Document document);
}
