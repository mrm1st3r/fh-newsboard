package de.fh_bielefeld.newsboard.dao;

import de.fh_bielefeld.newsboard.model.Classification;
import de.fh_bielefeld.newsboard.model.ExternalModule;
import de.fh_bielefeld.newsboard.model.Sentence;

import java.util.List;

/**
 * Data access interface for classifications.
 */
public interface ClassificationDao {

    Classification get(Sentence sentence, ExternalModule module);

    int update(Classification classification);

    int create(Classification classification);

    List<Classification> findForSentence(Sentence sentence);

    List<Classification> findForModule(ExternalModule module);
}
