package de.fhbielefeld.newsboard.xml;

import java.util.Calendar;

/**
 * Callback handler for reading new documents from XML.
 */
public interface DocumentParsedHandler {

    void onDocumentParsed(String title,
                          String author,
                          String source,
                          Calendar creationTime,
                          Calendar crawlTime,
                          String rawText);
}
