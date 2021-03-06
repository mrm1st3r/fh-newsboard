package de.fhbielefeld.newsboard.model.module

import de.fhbielefeld.newsboard.model.access.AccessId
import spock.lang.Specification

class ModuleIdTest extends Specification {

    def "should test for equality"() {
        given:
        def mod1 = new ModuleId("a")
        def mod2 = new ModuleId("b")
        def mod3 = new ModuleId("a")
        def access = new AccessId("a")

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
