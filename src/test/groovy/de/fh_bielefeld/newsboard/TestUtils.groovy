package de.fh_bielefeld.newsboard

import org.springframework.jdbc.core.JdbcTemplate

/**
 * Utility class for general testing stuff.
 */
final class TestUtils {

    static InputStreamReader sampleXml(String name) {
        def xml = new InputStreamReader(getClass().getResourceAsStream("/" + name + ".xml"))
        return xml
    }

    static cleanupDatabase(JdbcTemplate jdbcTemplate, List<Integer> sentenceIds,
                           List<Integer> documentIds, List<String> moduleIds) {
        for (Integer id : sentenceIds) {
            jdbcTemplate.update("DELETE FROM sentence WHERE sentence_id = " + id)
        }
        for (Integer id : documentIds) {
            jdbcTemplate.update("DELETE FROM document WHERE document_id = " + id)
        }
        for(String id : moduleIds) {
            jdbcTemplate.update("DELETE FROM module WHERE module_id = '" + id + "'")
        }
    }
}
