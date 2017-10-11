package de.fhbielefeld.newsboard.model.document;

import de.fhbielefeld.newsboard.model.Repository;
import io.vavr.collection.List;

/**
 * Data access interface for sentences.
 */
public interface SentenceDao extends Repository {

    int create(Sentence sentence, Document document);

    List<Sentence> findForDocument(DocumentId document);
}
