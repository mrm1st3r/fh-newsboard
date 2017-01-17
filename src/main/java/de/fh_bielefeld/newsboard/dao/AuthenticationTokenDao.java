package de.fh_bielefeld.newsboard.dao;

import de.fh_bielefeld.newsboard.model.AuthenticationToken;
import de.fh_bielefeld.newsboard.model.ExternalModule;

import java.util.List;

/**
 * Created by felixmeyer on 11.12.16.
 */
public interface AuthenticationTokenDao {
    public AuthenticationToken getTokenWithId(int id);
    public List<AuthenticationToken> getAllTokenForModule(ExternalModule externalModule);
    public int updateAuthenticationToken(AuthenticationToken authToken);
    public int insertAuthenticationToken(AuthenticationToken authToken);
}
