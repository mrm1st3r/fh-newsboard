package de.fh_bielefeld.newsboard.dao;

import de.fh_bielefeld.newsboard.model.Classification;
import de.fh_bielefeld.newsboard.model.ExternalModule;
import de.fh_bielefeld.newsboard.model.Sentence;

import java.util.List;

/**
 * Created by felixmeyer on 11.12.16.
 */
public interface ClassificationDao {
    public Classification getClassification(Sentence sentence, ExternalModule module);
    public List<Classification> getAllClassificationsForSentence(Sentence sentence);
    public List<Classification> getAllClassificationsFromModule(ExternalModule module);
    public int updateClassification(Classification classficiation);
    public int insertClassification(Classification classification);
}
