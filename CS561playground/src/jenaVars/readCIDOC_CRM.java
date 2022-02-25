/**
 * 
 */
package jenaVars;

import java.io.FileInputStream;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;

/**
 * @author Yannis Tzitzikas (yannistzitzik@gmail.com)
 *
 */
public class readCIDOC_CRM {

	static void readCIDOC() {
		//String filenameToRead = "cidoc_crm_v6.2-2018April.rdfs.xml";
		
		String filenameToRead = "/ontologies/cidoc_crm_v6.2-2018April.rdfs.xml";
		
		System.out.println("\n--[Reading from file:" + filenameToRead);
        Model model2 = ModelFactory.createDefaultModel();
        try {
        	model2.read(new FileInputStream(filenameToRead), "http://ex.org/", "RDF/XML");
        } catch (Exception e) {
            System.out.println(e);
        }
        System.out.println("\n--[Print what was read from the file:" + filenameToRead);
        model2.write(System.out);
        System.out.println("\n--[Print in Turtle what was read from the file:" + filenameToRead);
        model2.write(System.out, "TURTLE");
        
        
        System.out.println(model2);
        
	}
	
	public static void main(String[] lala) {
		readCIDOC();
	}
   

}
