package de.fhbielefeld.newsboard.model

import spock.lang.Specification

class ExternalModuleTest extends Specification {

    def "should test for equality"() {
        given:
        def m1 = new ExternalModule("mod1", "Module 1", "tester", "bla", new AccessReference(""))
        def m2 = new ExternalModule("mod2", "Module 1", "tester", "bla", new AccessReference(""))
        def m3 = new ExternalModule("mod1", "Module 1", "tester", "bla", new AccessReference(""))
        def d = new ExternalDocument(42, "Test", "Foo", null)

        expect:
        m1 != m2
        m2 != m3
        m1 == m3
        m1.hashCode() == m3.hashCode()
        m1.equals(null)
        m1 != d
    }
}
