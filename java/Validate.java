import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import org.xml.sax.SAXException;

/**
 * A sample application which shows how to perform a
 * XML document validation.
 */

public class Validate {
  public static void main(String[] args) {
    try {
      // define the type of schema - we use W3C:
      String schemaLang = "http://www.w3.org/2001/XMLSchema";

      // get validation driver:
      SchemaFactory factory = SchemaFactory.newInstance(schemaLang);

      // create schema by reading it from an XSD file:
      Schema schema = factory.newSchema(new StreamSource("sample.xsd"));
      Validator validator = schema.newValidator();

      // at last perform validation:
      validator.validate(new StreamSource("sample.xml"));

    }catch (SAXException ex) {
      System.out.println("Something went wrong ...");
      ex.printStackTrace();
      // we are here if the document is not valid:
      // ... process validation error...
    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }
}
