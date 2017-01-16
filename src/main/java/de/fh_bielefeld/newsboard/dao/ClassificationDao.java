package de.fh_bielefeld.newsboard.dao;

import de.fh_bielefeld.newsboard.model.Classification;
import de.fh_bielefeld.newsboard.model.Document;
import de.fh_bielefeld.newsboard.model.ExternModule;
import de.fh_bielefeld.newsboard.model.Sentence;

import java.util.List;

/**
 * Created by felixmeyer on 11.12.16.
 */
public interface ClassificationDao {
    public Classification getClassification(Sentence sentence, ExternModule module);
    public List<Classification> getAllClassificationsForSentence(Sentence sentence);
    public List<Classification> getAllClassificationsFromModule(ExternModule module);
    public int updateClassification(Classification classficiation);
    public int insertClassification(Classification classification);
}
