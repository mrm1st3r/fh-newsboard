package de.fh_bielefeld.newsboard.xml

import org.xml.sax.SAXParseException
import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Unroll

import javax.xml.XMLConstants
import javax.xml.transform.stream.StreamSource
import javax.xml.validation.Schema
import javax.xml.validation.SchemaFactory
import javax.xml.validation.Validator

/**
 * Testing the XML schema for any documents against predefined sample files.
 */
class DocumentSchemaTest extends Specification {

    @Shared
    private Validator validator

    private void setupSpec() {
        SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI)
        URL schemaFile = getClass().getResource("/document.xsd")
        Schema schema = factory.newSchema(schemaFile)
        validator = schema.newValidator()
    }

    @Unroll
    def "should validate #filename"() {
        given:
        def testDocument = new StreamSource(getClass().getResourceAsStream(filename))

        expect:
        validator.validate(testDocument)

        where:
        filename | _
        "/valid_classified_document.xml"  | _
        "/valid_raw_document.xml" | _
        "/valid_new_classifications.xml" | _
        "/valid_document_list.xml" | _
    }

    @Unroll
    def "should not validate #filename"() {
        given:
        def testDocument = new StreamSource(getClass().getResourceAsStream(filename))

        when:
        validator.validate(testDocument)

        then:
        thrown(SAXParseException.class)

        where:
        filename | _
        "/invalid_raw_document.xml"  | _
    }
}
