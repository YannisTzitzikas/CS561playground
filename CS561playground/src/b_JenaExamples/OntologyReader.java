/**
 * 
 */
package b_JenaExamples;

import java.io.FileInputStream;
import java.io.InputStream;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;

/**
 * @author Yannis Tzitzikas (yannistzitzik@gmail.com)
 * Reading an ontology (here CIDOC CRM) from the folder Resources
 */
public class OntologyReader {

	 void readFromPath(String filenameToRead) {
		InputStream inputStream = getClass().getResourceAsStream(filenameToRead);
		Model model = ModelFactory.createDefaultModel();
        try {
             model.read(inputStream, "http://ex.org/", "RDF/XML");            
        } catch (Exception e) {
            System.out.println(e);
        }          
        System.out.println("\n--[Print what was read from the file:" + filenameToRead);
        model.write(System.out);
        System.out.println("\n--[Print in Turtle what was read from the file:" + filenameToRead);
        model.write(System.out, "TURTLE");
        System.out.println(model);
        
	}
	
	public static void main(String[] lala) {
		String v2018 = "/ontologies/cidoc_crm_v6.2-2018April.rdfs.xml";
		String v2021 = "/ontologies/CIDOC_CRM_v7.1.1.rdfs.xml";
		
		OntologyReader a = new OntologyReader();
		//a.readFromPath(v2018);
		a.readFromPath(v2021);
		
	}
   

}
