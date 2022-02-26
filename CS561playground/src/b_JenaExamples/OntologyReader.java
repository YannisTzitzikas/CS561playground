/**
 * 
 */
package b_JenaExamples;

import java.awt.Desktop;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URL;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;

import java.net.URI;

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
	 
	/**
	 * Calls webvowl to visualize an ontology
	 * @param url  the url of an ontology (should be web accessible)
	 */
	void visualize(String url) {
		//String v7 = "https://service.tib.eu/webvowl/#iri=https://cidoc-crm.org/rdfs/7.1.1/CIDOC_CRM_v7.1.1.rdfs";
		//String v6 ="https://service.tib.eu/webvowl/#iri=http://www.cidoc-crm.org/sites/default/files/cidoc_crm_v6.2-2018April.rdfs";
		
		String urlwithParam = "https://service.tib.eu/webvowl/#iri="+url;
		
		try {
			if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
			    Desktop.getDesktop().browse(new URI(urlwithParam));
			}
		} catch (Exception e) {
			System.out.println(e);
		}
		
			
	}
	
	public static void main(String[] lala) {
		// two versions of the CIDOC CRM ontology 
		String v2018 = "/ontologies/cidoc_crm_v6.2-2018April.rdfs.xml";
		String v2021 = "/ontologies/CIDOC_CRM_v7.1.1.rdfs.xml";
		
		OntologyReader a = new OntologyReader();
		//a.readFromPath(v2018);
		a.readFromPath(v2021);
		
		a.visualize("https://cidoc-crm.org/rdfs/7.1.1/CIDOC_CRM_v7.1.1.rdfs");
		
	}
   

}
