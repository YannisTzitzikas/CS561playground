/**
 * 
 */
package hifun;

import java.io.FileInputStream;

import org.apache.jena.ontology.Individual;
import org.apache.jena.ontology.OntClass;
import org.apache.jena.ontology.OntModel;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.util.iterator.ExtendedIterator;

/**
 * @author Yannis Tzitzikas (yannistzitzik@gmail.com)
 *
 */
public class readHifunRunningExample {

	public static void  readFileCreatedByJena() {
		String filenameToRead = "test.ttl";
		System.out.println("\n--[Reading from file:" + filenameToRead);
        Model model2 = ModelFactory.createDefaultModel();
        try {
        	model2.read(new FileInputStream(filenameToRead), "http://ex.org/", "TURTLE");
        } catch (Exception e) {
            System.out.println(e);
        }
        System.out.println("\n--[Print what was read from the file:" + filenameToRead);
        model2.write(System.out);
        System.out.println("\n--[Print in Turtle what was read from the file:" + filenameToRead);
        model2.write(System.out, "TURTLE");
	}
	
	public static void  readFileCreatedByProtege() {
		String filenameToRead = "exampleV3.ttl";
		System.out.println("\n--[Reading from file:" + filenameToRead);
        Model model2 = ModelFactory.createDefaultModel();
        try {
        	model2.read(new FileInputStream(filenameToRead), "http://ex.org/", "TURTLE");
        } catch (Exception e) {
            System.out.println(e);
        }
        System.out.println("\n--[Print what was read from the file:" + filenameToRead);
        
                
        model2.write(System.out);
        System.out.println("\n--[Print in Turtle what was read from the file:" + filenameToRead);
        model2.write(System.out, "TURTLE");
	}
     
	// based on: https://dzone.com/articles/jena-listing-all-classes-and
    static void printAllIndividuals() {
    	OntModel model = ModelFactory.createOntologyModel();
        model.read("exampleV3.ttl");
        // test begin
        
        //@SuppressWarnings("rawtypes")
		ExtendedIterator classes = model.listClasses();

		System.out.println("==========INSTANCES==========");
        while (classes.hasNext())
        {
          OntClass thisClass = (OntClass) classes.next();
          System.out.println("Found class: " + thisClass.toString());

          ExtendedIterator instances = thisClass.listInstances();

          while (instances.hasNext())
          {
            Individual thisInstance = (Individual) instances.next();
            System.out.println("  Found instance: " + thisInstance.toString());
          }
        }
        
        // test end

    }
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		//readFileCreatedByJena();
		readFileCreatedByProtege();
		
		// not tested:
		printAllIndividuals() ;
	}

}
