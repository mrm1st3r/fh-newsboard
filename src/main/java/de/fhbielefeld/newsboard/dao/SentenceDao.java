package de.fhbielefeld.newsboard.dao;

import de.fhbielefeld.newsboard.model.Document;
import de.fhbielefeld.newsboard.model.DocumentStub;
import de.fhbielefeld.newsboard.model.Sentence;

import java.util.List;

/**
 * Data access interface for sentences.
 */
public interface SentenceDao {

    int create(Sentence sentence, Document document);

    List<Sentence> findForDocument(DocumentStub document);
}
