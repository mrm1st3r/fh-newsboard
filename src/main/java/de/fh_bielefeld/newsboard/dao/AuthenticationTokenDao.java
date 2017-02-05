package de.fh_bielefeld.newsboard.dao;

import de.fh_bielefeld.newsboard.model.AuthenticationToken;
import de.fh_bielefeld.newsboard.model.ExternalModule;

import java.util.List;
import java.util.Optional;

/**
 * Data access interface for authentication tokens used by external modules.
 */
public interface AuthenticationTokenDao {

    AuthenticationToken get(int id);

    int update(AuthenticationToken authToken);

    int create(AuthenticationToken authToken);

    List<AuthenticationToken> findForModule(ExternalModule externalModule);

    Optional<AuthenticationToken> find(String token);
}
