package de.fh_bielefeld.newsboard.model;

/**
 * External Module providing 'bypass-content'.
 * Bypass content will not be classified but instead contains other information
 * that should be viewed in the newsboard.
 */
public class BypassModule extends ExternalModule {
    public BypassModule(String id, String name ,String author, String description) {
        super(id, name, author, description);
    }
}
