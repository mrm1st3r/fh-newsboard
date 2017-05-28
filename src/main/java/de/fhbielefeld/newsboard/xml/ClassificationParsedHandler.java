package de.fhbielefeld.newsboard.xml;

import java.util.OptionalDouble;

/**
 * Callback handler for reading new classifications from XML.
 */
public interface ClassificationParsedHandler {

    void onValueParsed(double value, OptionalDouble confidence);

    void onClassificationParsed(int documentId, String classifierId);
}
