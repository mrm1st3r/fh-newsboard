package de.fh_bielefeld.newsboard.model

import spock.lang.Specification

class ClassificationTest extends Specification {

    def "should test for equality"() {
        given:
        def mod1 = new ExternModule()
        mod1.setId("randomClassifier")
        def c1 = new Classification()
        c1.setSentenceId(1)
        c1.setExternModule(mod1)
        def c2 = new Classification()
        c2.setSentenceId(2)
        c2.setExternModule(mod1)
        def c3 = new Classification()
        c3.setSentenceId(1)
        c3.setExternModule(mod1)

        expect:
        c1 != null
        c1 != mod1
        c1 != c2
        c2 != c3
        c1 == c3
    }
}
