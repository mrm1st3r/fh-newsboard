package de.fhbielefeld.newsboard.model.document;

import com.google.common.collect.ImmutableList;
import de.fhbielefeld.newsboard.model.Repository;

/**
 * Data access interface for sentences.
 */
public interface SentenceDao extends Repository {

    int create(Sentence sentence, Document document);

    ImmutableList<Sentence> findForDocument(DocumentId document);
}
