package de.fhbielefeld.newsboard.xml;

/**
 * Exception that might be thrown by the XML services.
 */
public class XmlException extends Exception {

    XmlException(Exception cause) {
        super(cause);
    }
}
