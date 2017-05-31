package de.fhbielefeld.newsboard.model;

/**
 * Base class for all aggregate roots.
 */
public interface Aggregate<T extends Aggregate<T>> extends Entity<T> {
}
