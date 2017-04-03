package de.fh_bielefeld.newsboard.model

import spock.lang.Specification

class ExternalModuleTest extends Specification {

    def "should test for equality"() {
        given:
        def m1 = new ExternalModule()
        def m2 = new ExternalModule()
        def m3 = new ExternalModule()
        def d = new ExternalDocument(42, "Test", "Foo", null)
        m1.setId("test-crawler")
        m2.setId("test-classifier")
        m3.setId("test-crawler")

        expect:
        m1 != m2
        m2 != m3
        m1 == m3
        m1 != null
        m1 != d
    }
}
