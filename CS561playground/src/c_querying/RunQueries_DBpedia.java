package c_querying;



import java.io.BufferedReader;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.Authenticator;
import java.net.MalformedURLException;
import java.net.PasswordAuthentication;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

import org.apache.jena.query.Dataset;
import org.apache.jena.query.DatasetFactory;
import org.apache.jena.query.ParameterizedSparqlString;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.apache.jena.query.ResultSetFormatter;

/**
 * Examples of sending SPARQL queries to DBpedia (the code also uses Jena)
 * @author Yannis Tzitzikas (yannistzitzik@gmail.com)
 *
 */

public class RunQueries_DBpedia {
	
	public static void queryDBpedia() {
		
		//Q1
		// Through a local (empty) Jena model and a SPARQL query that uses SERVICE
		System.out.println("q1: Getting 10 persons from dbpedia");
	    String q1 = "SELECT DISTINCT ?person WHERE { SERVICE <http://dbpedia.org/sparql> {  	?person a <http://xmlns.com/foaf/0.1/Person> . } } LIMIT 10";
	    System.out.println("q1: " + q1 );
	    System.out.println("q1 results:");
	    Dataset dataset = DatasetFactory.create();  // an empty dataset
	    ResultSet q1Res = QueryExecutionFactory.create(q1, dataset).execSelect(); // for executing this SELECT query
   	   	while (q1Res.hasNext()) {
		    QuerySolution qs = q1Res.nextSolution();
		    System.out.println(qs.toString());
		}
		
   	   	
   	   	//Q2
   		// Directly sending the SPARQL query
   	   	System.out.println("\n\n");
   	    System.out.println("q2: Getting 10 subclasses of dbpedia Agent");
	    String q2 = "prefix rdfs:    <http://www.w3.org/2000/01/rdf-schema#>\n"
  				+ "prefix rdf: 	   <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n"
                + "PREFIX dbo:     <http://dbpedia.org/ontology/>"
                + "\n"
                + "select  ?c where {\n"
                + "  ?c rdfs:subClassOf dbo:Agent .\n"
                + "  } limit 20";
	    System.out.println("q2: " + q2 );
	    System.out.println("q2 results:");
   	   	
   		ParameterizedSparqlString q2p = new ParameterizedSparqlString(q2);
  		QueryExecution q2exec = QueryExecutionFactory.sparqlService("http://dbpedia.org/sparql", q2p.asQuery());
  		ResultSet q2results = q2exec.execSelect();
         
        if (q2results!=null) 
         	ResultSetFormatter.out(q2results);

       
		
		//Q3
   		// Directly sending the SPARQL query
        System.out.println("\n\n");
   	    System.out.println("q3: Getting  a resource about a medication and its english abstract ");
	    String q3 = "prefix rdfs:    <http://www.w3.org/2000/01/rdf-schema#>\n"
                + "PREFIX dbo:     <http://dbpedia.org/ontology/>"
                + "\n"
                + "select distinct ?resource ?abstract where {\n"
                + "  ?resource rdfs:label 'Ibuprofen'@en.\n"
                + "  ?resource dbo:abstract ?abstract.\n"
                + "  FILTER (lang(?abstract) = 'en')}";
	    System.out.println("q3: " + q2 );
	    System.out.println("q3 results:");
	    ParameterizedSparqlString q3p = new ParameterizedSparqlString(q3);
	    QueryExecution q3exec = QueryExecutionFactory.sparqlService("http://dbpedia.org/sparql", q3p.asQuery());
        ResultSet q3r = q3exec.execSelect();
        
        int i=1;
        while (q3r.hasNext()) {
        	System.out.println(i + ": " + q3r.next().toString());
            //System.out.println(i + " (abstract): " + results.next().get("abstract").toString());
            i++;
        }
        
		
			}

	
		
	
	
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args)  {

    	try {
    		queryDBpedia();
    	}  	catch (Exception e) {
        	System.out.println(e);
        }
    }
}
