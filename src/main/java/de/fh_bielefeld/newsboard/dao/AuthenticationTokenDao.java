package de.fh_bielefeld.newsboard.dao;

import de.fh_bielefeld.newsboard.model.AuthenticationToken;
import de.fh_bielefeld.newsboard.model.ExternalModule;

import java.util.List;
import java.util.Optional;

/**
 * Data access interface for authentication tokens used by external modules.
 */
public interface AuthenticationTokenDao {
    AuthenticationToken getTokenWithId(int id);
    List<AuthenticationToken> getAllTokenForModule(ExternalModule externalModule);
    Optional<AuthenticationToken> findToken(String token);
    int updateAuthenticationToken(AuthenticationToken authToken);
    int insertAuthenticationToken(AuthenticationToken authToken);
}
