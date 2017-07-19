package de.fh_bielefeld.newsboard.dao;

import de.fh_bielefeld.newsboard.model.Document;
import de.fh_bielefeld.newsboard.model.Sentence;
import de.smartsquare.ddd.annotations.DDDRepository;

import java.util.List;

/**
 * Data access interface for sentences.
 */
@DDDRepository
public interface SentenceDao {

    Sentence get(int id);

    int update(Sentence sentence);

    int create(Sentence sentence, Document document);

    List<Sentence> findForDocument(Document document);
}
