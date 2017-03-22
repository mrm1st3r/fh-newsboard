package de.fh_bielefeld.newsboard

/**
 * Utility class for general testing stuff.
 */
final class TestUtils {

    static InputStreamReader sampleXml(String name) {
        def xml = new InputStreamReader(getClass().getResourceAsStream("/" + name + ".xml"))
        return xml
    }
}
