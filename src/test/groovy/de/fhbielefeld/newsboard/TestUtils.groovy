package de.fhbielefeld.newsboard

import de.fhbielefeld.newsboard.model.*
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
                           List<Integer> documentIds, List<String> moduleIds, List<Integer> externalDocumentIds = Collections.emptyList()) {
        for (Integer id : sentenceIds) {
            jdbcTemplate.update("DELETE FROM sentence WHERE sentence_id = " + id)
        }
        for (Integer id : documentIds) {
            jdbcTemplate.update("DELETE FROM document WHERE document_id = " + id)
        }
        for (Integer id : externalDocumentIds) {
            jdbcTemplate.update("DELETE FROM external_document WHERE ext_document_id = " + id)
        }
        for(String id : moduleIds) {
            jdbcTemplate.update("DELETE FROM module WHERE module_id = '" + id + "'")
        }
        jdbcTemplate.update("DELETE FROM access WHERE access_id = 'test-access'")
    }

    static sampleModule() {
        new ExternalModule("test_module", "Test module", "Tester", "Module for testing purpose", new AccessReference("test-access"))
    }

    static sampleSentence() {
        new Sentence(-1, 1, "Example text of a sentence object for testing purposes.")
    }

    static sampleExternalDocument(ExternalModule module) {
        new ExternalDocument(-1, "Test external document", "<body><h1>Testing html</h1></body>", module)
    }

    static sampleClassification(ExternalModule module, int sentenceId) {
        new Classification(sentenceId, module, 0.6, OptionalDouble.of(1))
    }

    static sampleAccess() {
        return new Access("test-access", new AccessRole("crawler"), "passphrase", "plain", true)
    }

    static sampleDocumentForDb(ExternalModule module) {
        ArrayList<Sentence> sentences = new ArrayList<>()
        for (int i = 0; i < 3; i++) {
            sentences.add(sampleSentence())
        }
        return new Document(-1, "Test document", "Test author", "Test source",
                new GregorianCalendar(2017, 6, 4), new GregorianCalendar(2010, 2, 1), module, sentences)
    }

    static sampleDocumentForXml() {
        def s1 = new Sentence(1, 1, "Lorem ipsum dolor sit amet.")
        def s2 = new Sentence(24, 2, "Die Würde des Tasters ist unanmenschbar.")
        s1.addClassification(new ModuleReference("a"), 1, OptionalDouble.empty())
        s2.addClassification(new ModuleReference("a"), 0.9, OptionalDouble.of(0.95))
        return new Document(42, "Wuppi Fluppi", "Hans Wurst", "http://example.com",
                new GregorianCalendar(2016, Calendar.NOVEMBER, 30), new GregorianCalendar(2016, Calendar.DECEMBER, 01),
                null, [s1, s2])
    }

    static emptyDocument(int id, List<Sentence> sentences) {
        return new Document(id, "", "", "", null, null, null, sentences)
    }
}
