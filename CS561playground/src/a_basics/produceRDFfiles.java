/**
 * 
 */
package a_basics;

import java.io.FileWriter;

/**
 * Produces a simple file in RDF (ttl format) that is loadable by Protege
 * (no dependency to any other library or code).
 * scenario1: producing a small dataset (literally)
 * scenario2: producing a synthetic dataset (algorithmically)
 * @author Yannis Tzitzikas (yannistzitzik@gmail.com)
 *
 */

public class produceRDFfiles {
	
	private static String headerStr =
			"@prefix : <http://www.ics.forth.gr/example#> .\r\n" + 
			"@prefix ex: <http://www.ics.forth.gr/example> .\r\n" + 
			"@prefix owl: <http://www.w3.org/2002/07/owl#> .\r\n" + 
			"@prefix rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> .\r\n" + 
			"@prefix tmp: <http://www.semanticweb.org/maria/ontologies/2021/9/running_example#> .\r\n" + 
			"@prefix xml: <http://www.w3.org/XML/1998/namespace> .\r\n" + 
			"@prefix xsd: <http://www.w3.org/2001/XMLSchema#> .\r\n" + 
			"@prefix rdfs: <http://www.w3.org/2000/01/rdf-schema#> .\r\n" + 
			"@prefix example: <http://www.ics.forth.gr/example#> .\r\n" + 
			"@base <http://www.ics.forth.gr/example> .\r\n" + 
			"\r\n" + 
			"<http://www.ics.forth.gr/example> rdf:type owl:Ontology .\n\n";
	
	
	/**
	 * Produces a small  RDF file in ttl format (it has a small schema and a few instances)
	 * @param filenameToWrite
	 */
	
	public static void scenario1(String filenameToWrite) {
		
		try {
			FileWriter fr = new FileWriter(filenameToWrite,false); // overwrite file if it exists
			fr.write(headerStr);
			String triplesStr ="example:Company rdf:type owl:Class .\r\n" + 
					"\r\n" + 
					"example:Location rdf:type owl:Class .\n" + 
					"example:Continent rdf:type owl:Class ;\n" + 
					"                  rdfs:subClassOf example:Location .\r\n" + 
					"example:locatedAt rdf:type owl:ObjectProperty ;\r\n" + 
					"                  rdfs:domain example:Location ;\r\n" + 
					"                  rdfs:range example:Location .\r\n" + 
					"example:price rdf:type owl:DatatypeProperty ;\r\n" + 
					"              rdfs:domain example:Company ;\r\n" + 
					"              rdfs:range xsd:float .\r\n" + 
					"example:America rdf:type owl:NamedIndividual ,\r\n" + 
					"                         example:Continent .\r\n" + 
					"example:Maxtor rdf:type owl:NamedIndividual ,\r\n" + 
					"                        example:Company ;\r\n" + 
					"               example:price \"59.939\"^^xsd:float ;\r\n" + 
					"               rdfs:comment \"Maxtor was an American computer hard disk drive manufacturer. Founded in 1982, it was the third largest hard disk drive manufacturer in the world before being purchased by Seagate in 2006\"@en .\r\n" + 
					"";
			fr.write(triplesStr);
			fr.close();
			System.out.println("Success (scenario1).");

		  } catch (Exception e) {
			  System.out.println(e);
		  }
		
		 
		  }
	
	/**
	 * Produces an RDF file algorithmically.
	 * We can control the size. We can load the output to protege to browse it
	 * @param filenameToWrite
	 */
	
	public static void scenario2(String filenameToWrite) {
		
		try {
			FileWriter fr = new FileWriter(filenameToWrite,false); // overwrite file if it exists
			
			fr.write(headerStr);
			
			int Nclassses=10;
			int NinstancesPerClass=10;
			//classes (i)  and instances (ij)
			fr.write("\nexample:topClass  rdf:type owl:Class .\n");
			for (int i=0; i<Nclassses;i++) {
				fr.write("\nexample:class"+i+ " rdf:type owl:Class; rdfs:subClassOf example:topClass .\n"); 
				
				for (int j=0; j<NinstancesPerClass;j++) {
					fr.write("example:individual"+i+j+ " rdf:type owl:NamedIndividual, example:class"+i+"  .\n"); 
				}
			}
			
			//properties and property instances
			int Nproperties=10;
			for (int i=0; i<Nproperties; i++) {
				fr.write("\nexample:property"+i+" rdf:type owl:ObjectProperty ; \n");
				fr.write("\t rdfs:domain example:class" + i%Nclassses + " ;\n");
				fr.write("\t rdfs:range example:class" + (i+2)%Nclassses + ". \n");
				
				//
				for (int j=0; j<NinstancesPerClass;j++) {
					fr.write("example:individual"+ (i%Nclassses)+j+ " example:property"+i  +"  example:individual"+ ((i+2)%Nclassses)+j + " .\n"); 
				}
			}
					
			fr.close();
			System.out.println("Success (scenario2).");

		  } catch (Exception e) {
			  System.out.println(e);
		  }
		
		 
		 }

	
	public static void main(String[] args) {
		scenario1("datafilesOutput/output-scenario1.ttl"); // simple
		scenario2("datafilesOutput/output-scenario2.ttl");    // synthetic

	}
}
