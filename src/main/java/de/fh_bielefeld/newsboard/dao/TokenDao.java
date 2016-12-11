package de.fh_bielefeld.newsboard.dao;

import de.fh_bielefeld.newsboard.model.Token;

import java.util.List;

/**
 * Created by felixmeyer on 11.12.16.
 */
public interface TokenDao {
    public Token getTokenWithId(int id);
    public List<Token> getAllTokenForModule(String moduleId);
}
