package de.fhbielefeld.newsboard.model

import spock.lang.Specification

class ModuleReferenceTest extends Specification {

    def "should test for equality"() {
        given:
        def mod1 = new ModuleReference("a")
        def mod2 = new ModuleReference("b")
        def mod3 = new ModuleReference("a")
        def access = new AccessReference("a")

        expect:
        mod1 == mod1
        mod1 != mod2
        mod2 != mod1
        mod2 != mod3
        mod1 == mod3
        mod1.hashCode() == mod3.hashCode()
        mod1 != access
    }
}
