package de.fh_bielefeld.newsboard.dao;

import de.fh_bielefeld.newsboard.model.Classification;

import java.util.List;

/**
 * Created by felixmeyer on 11.12.16.
 */
public interface ClassificationDao {
    public Classification getClassification(int sentenceId, int documentId, String moduleId);
    public List<Classification> getAllClassificationsForSentence(int sentId);
    public List<Classification> getAllClassificationsForDocument(int docId);
    public List<Classification> getAllClassificationsFromModule(String moduleId);
}
