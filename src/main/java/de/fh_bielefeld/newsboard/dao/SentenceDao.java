package de.fh_bielefeld.newsboard.dao;

import de.fh_bielefeld.newsboard.model.Document;
import de.fh_bielefeld.newsboard.model.Sentence;

import java.util.List;

/**
 * Created by felixmeyer on 11.12.16.
 */
public interface SentenceDao {
    public Sentence getSentenceWithId(int id);
    public List<Sentence> getAllSentencesInDocument(Document document);
    public int updateSentenceWithoutDocument(Sentence sentence);
    public int updateSentenceWithDocument(Sentence sentence, Document document);
    public int insertSentence(Sentence sentence, Document document);
}
