package de.fh_bielefeld.newsboard

/**
 * Utility class for general testing stuff.
 */
public final class TestUtils {

    public static InputStreamReader sampleXml(String name) {
        def xml = new InputStreamReader(getClass().getResourceAsStream("/" + name + ".xml"))
        return xml;
    }
}
