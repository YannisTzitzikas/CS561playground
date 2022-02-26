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
 * Reading an ontology from the folder Resources
 */
public class OntologyReader {

	 void readFromPath(String filenameToRead) {
		//String filenameToRead = "cidoc_crm_v6.2-2018April.rdfs.xml";
		//String filenameToRead = "/ontologies/cidoc_crm_v6.2-2018April.rdfs.xml";
		InputStream inputStream = getClass().getResourceAsStream(filenameToRead);
		Model model = ModelFactory.createDefaultModel();
        try {
        	//modelJ.read(new FileInputStream(filenameToRead), "http://ex.org/", "TURTLE");
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
		String opath = 	"/ontologies/cidoc_crm_v6.2-2018April.rdfs.xml";
		
		OntologyReader a = new OntologyReader();
		a.readFromPath(opath);
	}
   

}
