package de.fhbielefeld.newsboard.dao;

import de.fhbielefeld.newsboard.model.Classification;
import de.fhbielefeld.newsboard.model.Sentence;

import java.util.List;

/**
 * Data access interface for classifications.
 */
public interface ClassificationDao {

    int create(Classification classification);

    List<Classification> findForSentence(Sentence sentence);

}
