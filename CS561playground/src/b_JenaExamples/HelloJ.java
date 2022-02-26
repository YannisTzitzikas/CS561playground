/**
 * Examples of using Jena.
 * Some material is based on the examples given at  https://wiki.uib.no/info216/index.php/Java_Examples#Hello_Jena
 * Various changes and additions by Yannis Tzitzikas
 * Status: ok (it runs)
 */
package b_JenaExamples;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import org.apache.jena.datatypes.xsd.XSDDatatype;
import org.apache.jena.query.Dataset;
import org.apache.jena.query.DatasetFactory;
import org.apache.jena.query.Query;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.Statement;
import org.apache.jena.riot.Lang;
import org.apache.jena.riot.RDFDataMgr;
import org.apache.jena.sparql.vocabulary.FOAF;
import org.apache.jena.update.UpdateAction;
import org.apache.jena.rdf.model.Property;

//import org.apache.jena.vocabulary.FOAF;
import org.apache.jena.vocabulary.*;

import com.fasterxml.jackson.core.JsonGenerationException;
//import com.github.jsonldjava.utils.JsonUtils;
import com.github.jsonldjava.utils.JsonUtils;


class HelloJena {

	static Model model = ModelFactory.createDefaultModel(); // for making this accessible to several  methods
    static String base = "http://www.csd.uoc.gr/";  // uri base of entities to be created
    static String iriDbpedia = "http://dbpedia.org/resource/"; // just the uri of dbpedia
    
    /**
     * Creates programmatically a few triples, looping its statements, printing them and writing them to file,
     * and reading from file
     */
    
  
    public void createAFewTriples() {
    	Resource yannis = model.createResource(base + "~tzitzik");
		Resource cs561 =  model.createResource(base + "~hy561");
		Resource cs561_2022 =  model.createResource("https://elearn.uoc.gr/course/view.php?id=3112");
		
		Property teaches = 	 model.createProperty(base + "teaches");
		Property hasTeachings = model.createProperty(base + "hasTeachings");
		cs561.addProperty(hasTeachings, cs561_2022);
		yannis.addProperty(teaches, cs561_2022);
		
		yannis.addLiteral(FOAF.name, "Yannis Tzitzikas");
		yannis.addLiteral(FOAF.name, "Γιάννης Τζίτζικας@GR");
		//model.write(System.out, "TURTLE");
		
		// a few triples pointing to external sources
		Resource resCanada = model.createResource(iriDbpedia + "Canada");
        Resource resFrance = model.createResource(iriDbpedia + "France");
        resFrance.addProperty(RDFS.label, "Francia", "es");
        Property propVisited = model.createProperty(base + "visited");
        yannis.addProperty(propVisited, resCanada);
        yannis.addProperty(propVisited, resFrance);
        
        Property propPopEst = model.createProperty(base + "ontology/populationEstimate");
        resFrance.addProperty(propPopEst, "66644000", XSDDatatype.XSDinteger);
        
        //looping through statements
        System.out.println("\n--[Looping through statements]--");
        for (Statement stmt : model.listStatements().toList()) {
            System.out.println(stmt.toString());
        }
        System.out.println("-------------------------");
        
        //printing out the model
        System.out.println("\n--[System out in turtle]--");
        model.write(System.out, "TURTLE");
    
      
        //writing to file
        String filenameToWrite = "datafilesOutput/testTriplesAboutYT.ttl";
        String filenameToWrite2 = "datafilesOutput/testTriplesAboutYT.xml";
        System.out.println("\n--[Writting to file:" + filenameToWrite + "]--");
        try {
            model.write(new FileOutputStream(filenameToWrite), "TURTLE"); //ok
            model.write(new FileOutputStream(filenameToWrite2), "RDF/XML"); //ok
            
        } catch (Exception e) {
            System.out.println(e);
        }
        
        //reading from to file
        System.out.println("\n--[Reading from file:" + filenameToWrite);
        Model model2 = ModelFactory.createDefaultModel();
        try {
        	model2.read(new FileInputStream(filenameToWrite), "http://ex.org/", "TURTLE");
        } catch (Exception e) {
            System.out.println(e);
        }
        System.out.println("\n--[Print what was read from the file:" + filenameToWrite);
        model2.write(System.out);
        System.out.println("\n--[Print in Turtle what was read from the file:" + filenameToWrite);
        model2.write(System.out, "TURTLE");
    }
    
	/**
	 * Read a remote (Web-accessible) ttl file, loads it as a model,  and prints its contents
	 */
	 void readTriplesFromTheWeb() {
		   String urlstr="https://people.uib.no/sinoa/test.ttl";
		   
		   System.out.println("\n--[Readring triples from the web]");
		   System.out.println("from: "+ urlstr);
	        Model model = ModelFactory.createDefaultModel();
	        try {
	            URL url = new URL(urlstr);  
	            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection(); 
	            InputStream is = urlConnection.getInputStream();
	            model.read(is, "http://ex.org/", "TURTLE");
	        } catch (Exception e) {
	            System.out.println(e);
	        }
	       
	        model.write(System.out);
	    }
	
	/**
	 *  Dataset creation and querying with SPARQL
	 */
	void testSPARQLqueries() {
		System.out.println("--[SPARQL Insert Data]--");
		Dataset dataset = DatasetFactory.create();
		
		// Inserts data through SPARQL
		UpdateAction.parseExecute(""
				+ "PREFIX csd: <http://www.csd.uoc.gr/~>"
				+ "INSERT DATA {"
				+ "    csd:tzitzik csd:teaches csd:561 . "
				+ "    GRAPH <http://ex.org/personal#Graph> {"
				+ "        csd:tzitzik csd:age '18' . "
				+ "    }"
				+ "}", dataset);
	
		

		System.out.println("\n--[all contents of the dataset]");
		RDFDataMgr.write(System.out, dataset, Lang.TRIG);
		
		//To output only the default graph use:
		System.out.println("\n--[the contents of the default graph only]--");
		dataset.getDefaultModel().write(System.out, "TURTLE");
		
		
		// Example of querying the dataset using SPARQL
		System.out.println("\n--[Basic SELECT query]--");
		ResultSet resultSet = QueryExecutionFactory
		        .create(""
		            + "SELECT ?s ?p ?o WHERE {"
		            + "    ?s ?p ?o ."
		            + "}", dataset)
		        .execSelect();

		//System.out.println("\nResult Set:\n" + resultSet.toString());
		resultSet.forEachRemaining(qsol -> System.out.println(qsol.toString()));
		
		// Convert the ResultSet into a JSON object
		System.out.println("\n--[Convert the ResultSet into a JSON object ]--");
		ResultSet resultSetB = QueryExecutionFactory
		        .create(""
		            + "SELECT ?s ?p ?o WHERE {"
		            + "    ?s ?p ?o ."
		            + "}", dataset)
		        .execSelect();
		List<Map> jsonList = new Vector<Map>();
	    while (resultSetB.hasNext()) {
	        QuerySolution qsol = resultSetB.nextSolution();
	        Iterator<String> varNames = qsol.varNames();
	        Map<String, Object> jsonMap = new HashMap<String, Object>();
	        while (varNames.hasNext()) {
	            String varName = varNames.next();
	            jsonMap.put(varName, qsol.get(varName).toString());
	            System.out.println(">>>>putting:"+varName +"-->" +  qsol.get(varName).toString());
	        }
	        jsonList.add(jsonMap);
	    }
	    try {
			System.out.println(JsonUtils.toPrettyString(jsonList));
		} catch (JsonGenerationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("JSON list of maps:\n" + jsonList);
	    //  System.out.println("\n\n");
	    
	    // SELECT query with Query object
	    System.out.println("\n--[SELECT query with Query object]--");
	    Query query = QueryFactory.create(""
	            + "SELECT ?s ?p ?o WHERE {"
	            + "    ?s ?p ?o ."
	            + "}");
	    QueryExecution queryExecution = QueryExecutionFactory.create(query, dataset);   
	    ResultSet resultSet2 = queryExecution.execSelect();
	    while (resultSet2.hasNext()) {
	        QuerySolution qs = resultSet2.nextSolution();
	        System.out.println(qs.toString());
	    }
	    
	    
	    System.out.println("\n--[SELECT query from remote SPARQL endpoint ]--");
	    
	    //It gets 10 persons from dbpedia
	    String tmpQuery = "SELECT DISTINCT ?person WHERE { SERVICE <http://dbpedia.org/sparql> {  	?person a <http://xmlns.com/foaf/0.1/Person> . } } LIMIT 10";
	    ResultSet resultSet3 = QueryExecutionFactory.create(tmpQuery, dataset).execSelect();
   	   	while (resultSet3.hasNext()) {
		    QuerySolution qs = resultSet3.nextSolution();
		    System.out.println(qs.toString());
		}
	
	
		System.out.println("\n\n--[Basic DESCRIBE query]--");
		Model modelByDescribe = QueryExecutionFactory.create(""
	            //+ "DESCRIBE <" + iriDbpedia + "France>"
	            + "DESCRIBE <http://www.csd.uoc.gr/~tzitzik>" 
	            + "", dataset).execDescribe();
		//
		System.out.println("The Dataset:");
		RDFDataMgr.write(System.out, dataset, Lang.TRIG);
		//System.out.println(">>>>>>>>>>>>"+dataset.toString());
		//		dataset.write(System.out,"TURTLE");
		System.out.println("The result of the Describe query:");
		modelByDescribe.write(System.out, "TURTLE");
	}

    public static void main(String[] args) {
    	
    	System.out.println("Examples with Jena for managing RDF");	
   	 
    	HelloJena hj = new HelloJena();
       
    	 hj.createAFewTriples(); // ok
    	 hj.readTriplesFromTheWeb(); // ok
    	 hj.testSPARQLqueries();     // ok
		}

		
	
}
