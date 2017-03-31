package de.fh_bielefeld.newsboard.dao;

import de.fh_bielefeld.newsboard.model.Document;
import de.fh_bielefeld.newsboard.model.Sentence;

import java.util.List;

/**
 * Data access interface for sentences.
 */
public interface SentenceDao {

    int create(Sentence sentence, Document document);

    List<Sentence> findForDocument(Document document);
}
