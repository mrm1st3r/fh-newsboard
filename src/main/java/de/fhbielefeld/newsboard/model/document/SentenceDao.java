package de.fhbielefeld.newsboard.model.document;

import java.util.List;

/**
 * Data access interface for sentences.
 */
public interface SentenceDao {

    int create(Sentence sentence, Document document);

    List<Sentence> findForDocument(DocumentId document);
}
