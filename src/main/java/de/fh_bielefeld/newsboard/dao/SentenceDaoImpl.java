package de.fh_bielefeld.newsboard.dao;

import de.fh_bielefeld.newsboard.model.Sentence;

import java.util.List;

/**
 * Created by felixmeyer on 17.12.16.
 */
public class SentenceDaoImpl implements SentenceDao {
    @Override
    public Sentence getSentenceWithId(int id) {
        return null;
    }

    @Override
    public List<Sentence> getAllSentencesInDocument(int documentId) {
        return null;
    }

    @Override
    public int updateSentence(Sentence sentence) {
        return 0;
    }

    @Override
    public int insertSentence(Sentence sentence) {
        return 0;
    }
}
