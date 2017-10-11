package de.fhbielefeld.newsboard

import com.google.common.collect.ImmutableList
import de.fhbielefeld.newsboard.model.ExternalDocument
import de.fhbielefeld.newsboard.model.access.Access
import de.fhbielefeld.newsboard.model.access.AccessId
import de.fhbielefeld.newsboard.model.access.AccessRole
import de.fhbielefeld.newsboard.model.document.ClassificationValue
import de.fhbielefeld.newsboard.model.document.Document
import de.fhbielefeld.newsboard.model.document.DocumentClassification
import de.fhbielefeld.newsboard.model.document.DocumentId
import de.fhbielefeld.newsboard.model.document.DocumentMetaData
import de.fhbielefeld.newsboard.model.document.Sentence
import de.fhbielefeld.newsboard.model.module.ExternalModule
import de.fhbielefeld.newsboard.model.module.ModuleId
import io.vavr.collection.Iterator
import io.vavr.collection.List
import org.springframework.jdbc.core.JdbcTemplate

/**
 * Utility class for general testing stuff.
 */
final class TestUtils {

    static InputStreamReader sampleXml(String name) {
        def xml = new InputStreamReader(getClass().getResourceAsStream("/" + name + ".xml"))
        return xml
    }

    static cleanupDatabase(JdbcTemplate jdbcTemplate,
                           java.util.List<Integer> sentenceIds,
                           java.util.List<Integer> documentIds,
                           java.util.List<String> moduleIds,
                           java.util.List<Integer> externalDocumentIds = Collections.emptyList()) {
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
        new ExternalModule(new ModuleId("test_module"), "Test module", "Tester", "Module for testing purpose", new AccessId("test-access"))
    }

    static sampleSentence() {
        new Sentence(-1, 1, "Example text of a sentence object for testing purposes.")
    }

    static sampleExternalDocument(ExternalModule module) {
        new ExternalDocument(-1, "Test external document", "<body><h1>Testing html</h1></body>", module.getId())
    }

    static sampleAccess() {
        return new Access(new AccessId("test-access"), new AccessRole("crawler"), "passphrase", "plain", true)
    }

    static sampleDocumentForDb(ExternalModule module) {
        List<Sentence> sentences = Iterator.range(0, 3).map({i -> sampleSentence()}).toList()
        return new Document(
                new DocumentMetaData("Test document", "Test author", "Test source",
                new GregorianCalendar(2017, 6, 4),
                new GregorianCalendar(2010, 2, 1),
                module.getId()),
                sentences)
    }

    static sampleDocumentForXml() {
        def s1 = new Sentence(1, 1, "Lorem ipsum dolor sit amet.")
        def s2 = new Sentence(24, 2, "Die Würde des Tasters ist unanmenschbar.")
        def document = new Document(new DocumentId(42),
                new DocumentMetaData("Wuppi Fluppi", "Hans Wurst", "http://example.com",
                        new GregorianCalendar(2016, Calendar.NOVEMBER, 30),
                        new GregorianCalendar(2016, Calendar.DECEMBER, 01),
                        new ModuleId("test-module")), List.ofAll([s1, s2]))
        return document
    }

    static emptyDocument(int id, List<Sentence> sentences) {
        return new Document(new DocumentId(id),
                new DocumentMetaData("Wuppi Fluppi", "Hans Wurst", "http://example.com",
                        new GregorianCalendar(2016, Calendar.NOVEMBER, 30),
                        new GregorianCalendar(2016, Calendar.DECEMBER, 01),
                        new ModuleId("test-module")),
                List.ofAll(sentences))
    }

    static java.util.List<DocumentClassification> classificationsForDocument(Document document) {
        def c = document.addClassification(new ModuleId("a"), ImmutableList.copyOf([
                ClassificationValue.of(1),
                ClassificationValue.of(0.9, 0.95)
        ]))
        return Collections.singletonList(c)
    }
}
