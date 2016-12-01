import spock.lang.Shared
import spock.lang.Specification

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

    private void setup() {
        SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI)
        URL schemaFile = getClass().getResource("document.xsd")
        Schema schema = factory.newSchema(schemaFile)
        validator = schema.newValidator()
    }

    def "should validate classificated document"() {
        given:
        def testDocument = new StreamSource(getClass().getResourceAsStream("classificated_document.xml"))

        expect:
        validator.validate(testDocument)
    }
}
