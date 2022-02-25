/**
 * 
 */
package querying;

// Status: na oloklhrwsw ton kwdika sthn arxh pou stelnei
//   query sth dbpedia




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

import org.apache.jena.query.ParameterizedSparqlString;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.ResultSet;
import org.apache.jena.query.ResultSetFormatter;

public class RunQuery {
	
	public static void queryDBpedia() {
		ParameterizedSparqlString qs = new ParameterizedSparqlString(""
                + "prefix rdfs:    <http://www.w3.org/2000/01/rdf-schema#>\n"
                + "PREFIX dbo:     <http://dbpedia.org/ontology/>"
                + "\n"
                + "select distinct ?resource ?abstract where {\n"
                + "  ?resource rdfs:label 'Ibuprofen'@en.\n"
                + "  ?resource dbo:abstract ?abstract.\n"
                + "  FILTER (lang(?abstract) = 'en')}");


		//incomplete:
		ParameterizedSparqlString tmp = new ParameterizedSparqlString(""
                + "prefix rdfs:    <http://www.w3.org/2000/01/rdf-schema#>\n"
				+ "prefix rdf: 	   <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n"
                + "PREFIX dbo:     <http://dbpedia.org/ontology/>"
                + "\n"
                + "select  ?c where {\n"
                + "  ?c rdfs:subClassOf dbo:Agent .\n"
                + "  }");
		
		System.out.println(qs.toString());
		
		//QueryExecution exec = QueryExecutionFactory.sparqlService("http://dbpedia.org/sparql", tmp.asQuery());
		QueryExecution exec = QueryExecutionFactory.sparqlService("http://dbpedia.org/sparql", qs.asQuery());
        
        
        ResultSet results = exec.execSelect();
        
        System.out.println(results);

        while (results.hasNext()) {

            System.out.println(results.next().get("abstract").toString());
        }

        if (results!=null) 
        	ResultSetFormatter.out(results);
	}

	
		
	/*
	 * Μπορείς για αρχή να δοκιμάσεις κάτι πολύ απλό και γρήγορο: Ο χρήστης θα δίνει σαν είσοδο ένα ψάρι π.χ. "τσιπούρα", "salmon",  κλπ. και θα παίρνει πίσω πληροφορίες που υπάρχουν για το συγκεκριμένο ψάρι από ένα SPARQL endpoint (πχ από το endpoint του MarineTLO Warehouse που έχουμε εδώ στο ΙΤΕ). 

	Το SPARQL endpoint του MarineTLO warehouse βρίσκεται στο 
	http://62.217.127.213:8890/sparql 
	και για να συνδεθείς τα credentials είναι:

	username: imarine
	password: imarine

	Για παράδειγμα, για να εμφανίσεις όλες τις πιθανές ονομασίες ενός ψαριού που λέγεται "τσιπούρα" μπορείς να τρέξεις το query (πρόσεξε σε ποια σημεία του query δίνουμε τη λέξη):

	define input:inference <http://www.ics.forth.gr/isl/TLObasedDataWarehouseV3Rules>
	select distinct str(?name)
	from <http://www.ics.forth.gr/isl/TLObasedDataWarehouseV3>
	where{
	{ ?sp <http://www.fishbase.org/entity#hasCommonName> ?commonNameNode .
	?commonNameNode <http://ics.forth.gr/Ontology/MarineTLO/imarine#assignedName> ?cname FILTER regex(?cname, "τσιπούρα", "i") .
	?sp <http://www.fishbase.org/entity#hasCommonName> ?cc .
	?cc <http://ics.forth.gr/Ontology/MarineTLO/imarine#assignedName> ?name}
	UNION 
	{ ?sp <http://www.fishbase.org/entity#hasScientificName> ?sciNameNode .
	?sciNameNode <http://ics.forth.gr/Ontology/MarineTLO/imarine#assignedName> ?cname FILTER regex(?cname, "τσιπούρα", "i") .
	?sp <http://www.fishbase.org/entity#hasScientificName> ?ss.
	?ss<http://ics.forth.gr/Ontology/MarineTLO/imarine#assignedName> ?name } 
	}
	 */


	public static void queryMarineTLO()  throws UnsupportedEncodingException, MalformedURLException, IOException {
		String endpoint = "http://62.217.127.213:8890/sparql";
        final String username = "imarine";
        final String password = "imarine";
        String sparqlQuery = "define input:inference <http://www.ics.forth.gr/isl/TLObasedDataWarehouseV3Rules> \n"
                + "select ?sp \n"
                + "from <http://www.ics.forth.gr/isl/TLObasedDataWarehouseV3> \n"
                + "where{?sp rdf:type ?species FILTER regex(?sp, \"salmon\", \"i\")} ";

        String sparqlQueryURL = endpoint + "?query=" + URLEncoder.encode(sparqlQuery, "utf8");

        URL url = new URL(sparqlQueryURL);
        URLConnection con = url.openConnection();
        String xml_content = "application/sparql-results+xml";
        con.setRequestProperty("ACCEPT", xml_content);

        Authenticator.setDefault(new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password.toCharArray());
            }
        });

        InputStream is = con.getInputStream();
        InputStreamReader isr = new InputStreamReader(is, "utf8");
        BufferedReader in = new BufferedReader(isr);

        String input;
        String resultsString = "";
        while ((input = in.readLine()) != null) {
            resultsString = resultsString + input + "\n";
        }

        in.close();
        isr.close();
        is.close();

        System.out.println("# SPARQL query was executed successfully!");
        System.out.println("# XML RESULT: ");
        System.out.println(resultsString);
	
	}
	
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args)  {

    	try {
    	
    		//queryMarineTLO();
    		queryDBpedia();
    	}  	catch (Exception e) {
        	System.out.println(e);
        }
    }
}
