package de.fh_bielefeld.newsboard.xml;

import java.util.OptionalDouble;

/**
 * Callback handler for reading new classifications from XML.
 */
public interface ClassificationParsedHandler {

    void onClassificationParsed(int documentId, int sentenceId, String classifier, double value, OptionalDouble confidence);
}
