package de.fhbielefeld.newsboard.model.access

import de.fhbielefeld.newsboard.TestUtils
import spock.lang.Specification

class AccessTest extends Specification {

    def "should correctly check equality"() {
        given:
        AccessRole role = new AccessRole("crawler")
        Access a1 = new Access("access-1", role, "password", "plain", true)
        Access a2 = new Access("access-1", role, "password", "plain", true)
        Access a3 = new Access("access-2", role, "password", "plain", true)
        Access a4 = new Access("access-2", role, "password1", "plain", true)
        Access a5 = new Access("access-2", role, "password1", "plain", false)

        expect:
        a1 == a2
        a1.hashCode() == a2.hashCode()
        a1 != a3
        a2 != a3
        a1 != role
        !a1.equals(null)
        a3 != a4
        a4 != a5
        a3 != a5
    }

    def "should set enabled state"() {
        given:
        def access = TestUtils.sampleAccess()

        expect:
        access.isEnabled()
        access.setEnabled(false)
        !access.isEnabled()
        access.setEnabled(true)
        access.isEnabled()
    }

    def "should set hash type"() {
        given:
        def access = TestUtils.sampleAccess()

        expect:
        access.setHashType("plain")
        "plain" == access.getHashType()
    }
}
