package init.transform;

import com.hp.hpl.jena.query.*;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.sdb.SDBFactory;
import com.hp.hpl.jena.sdb.Store;
import com.hp.hpl.jena.sdb.store.DatasetStore;
import com.hp.hpl.jena.update.GraphStore;
import com.hp.hpl.jena.update.GraphStoreFactory;
import com.hp.hpl.jena.update.UpdateAction;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class RDFDataStorage {

    // TODO: Bad practice - should be some other mechanism for setting this.
    public static String baseDir = "conf/";

    /* The list of prefixes required to work with triples */
    String project_ns = "prefix project:<http://circlesofsustainability.org/ontology/project#>";
    String rdf_ns = "prefix rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>";
    String ontology_ns = "prefix cos:<http://circlesofsustainability.org/ontology#>";


    /**
     * Loading the configuration for postgres database
     */
    Store store = SDBFactory.connectStore(baseDir + "sdb.ttl");

    /**
     * Dataset can be loaded from the file or the updated graphstore
     */
    Dataset dataset = DatasetStore.create(store);

    /**
     * Jena model to update the data into DB
     */
    Model model = SDBFactory.connectDefaultModel(store);


    /* Start raw data methods */


    /*
      * Generic RDF update. Requires the subject, predicate and old and new
      * objects of the update
      *
      * @param subject
      *
      * @param predicate
      *
      * @param oldObject
      *
      * @param newObject
      *
      * @return whether the insert is successful
      */
    public boolean dataUpdate(String subject, String predicate,
                              String oldObject, String newObject) {
        try {
            boolean deleteResult = dataDelete(subject, predicate, oldObject);
            if (!deleteResult)
                return deleteResult;
            boolean insertResult = dataInsert(subject, predicate, newObject);
            if (!insertResult)
                return insertResult;
        } catch (Exception e) {
            e.printStackTrace();
            return (false);
        }
        return (true);

    }

    /**
     * Generic RDF insert. Requires the subject, predicate and object of the
     * insert
     *
     * @param subject
     * @param predicate
     * @param object
     * @return whether the insert is successful
     */
    public boolean dataInsert(String subject, String predicate, String object) {
        try {

            Model model = SDBFactory.connectDefaultModel(store);
            GraphStore graphStore = GraphStoreFactory.create(model);
            String queryString = ontology_ns + "\n" + rdf_ns + "\n" + project_ns + "\n" + " INSERT DATA { " + subject
                    + " " + predicate + " " + object + " .}";

            System.out.println(queryString);
            /**
             * Parsing the query string to update the graph
             */
            UpdateAction.parseExecute(queryString, graphStore);
            System.out.println("Got here - inserted triple");
        } catch (Exception e) {
            e.printStackTrace();
            return (false);
        }
        return (true);

    }

    /**
     * Generic RDF data delete. Requires a predicate and object ID to identify
     * the triple to delete, and the subject of the delete
     *
     * @param subject
     * @param predicate
     * @param object
     * @return whether the delete is successful
     */
    public boolean dataDelete(String subject, String predicate, String object) {
        try {
            Model model = SDBFactory.connectDefaultModel(store);
            GraphStore graphStore = GraphStoreFactory.create(model);
            String queryString = ontology_ns + "\n" + rdf_ns + "\n" + project_ns + "\n" + " DELETE DATA {  " + subject
                    + " " + predicate + " " + object + " }";
            System.out.println(queryString);
            /**
             * Parsing the query string to update the graph
             */
            UpdateAction.parseExecute(queryString, graphStore);
            System.out.println("Got here - deleted triple");
        } catch (Exception e) {
            e.printStackTrace();
            return (false);
        }
        return (true);
    }

    /**
     * Generic RDF data query. Retrieves a Map of all predicates and objects
     * associated with a given subject URI.
     *
     * @param subject
     * @return whether the delete is successful
     */
    public Map dataQuery(String subject) {
        Map map = new HashMap();
        try {
            Model model = SDBFactory.connectDefaultModel(store);
            String queryString = ontology_ns + "\n" + rdf_ns + "\n" + project_ns + "\n" + " SELECT ?p ?o WHERE { "
                    + subject + " ?p ?o . }";
            System.out.println(queryString);

            Query query = QueryFactory.create(queryString);
            QueryExecution queryExect = QueryExecutionFactory.create(query, model);
            try {
                ResultSet result = queryExect.execSelect();
                //ResultSetFormatter.out(result) ;

                while (result.hasNext()) {

                    QuerySolution soln = result.nextSolution();

                    RDFNode pNode = soln.get("p");
                    RDFNode oNode = soln.get("o");

                    System.out.println(pNode.toString() + " " + oNode.toString());

                    map.put(pNode.toString(), oNode.toString());
                }
            } finally {
                queryExect.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return map;
    }


    /* End raw data methods */


    /* Start positioned data methods */


    /**
     * Generic RDF positioned delete. Deletes all predicates for a given subject.
     *
     * @param subject
     * @return whether the delete is successful
     */
    public boolean positionedDeleteAllPredicates(String subject) {
        try {
            Model model = SDBFactory.connectDefaultModel(store);
            GraphStore graphStore = GraphStoreFactory.create(model);
            String queryString = ontology_ns + "\n" + rdf_ns + "\n" + project_ns + "\n" +
                    " DELETE {" + subject + " ?p ?o} " +
                    " WHERE  {  " + subject + " ?p ?o . }";
            /**
             * Parsing the query string to update the graph
             */
            UpdateAction.parseExecute(queryString, graphStore);
            System.out.println("Got here - deleted triple");
        } catch (Exception e) {
            e.printStackTrace();
            return (false);
        }
        return (true);
    }


    /**
     * Generic RDF positioned delete. Deletes a single given predicate for a given subject.
     *
     * @param subject
     * @param predicate
     * @return whether the delete is successful
     */
    public boolean positionedDeleteSinglePredicate(String subject, String predicate) {
        try {
            Model model = SDBFactory.connectDefaultModel(store);
            GraphStore graphStore = GraphStoreFactory.create(model);
            String queryString = ontology_ns + "\n" + rdf_ns + "\n" + project_ns + "\n" +
                    " DELETE { ?s ?p ?o } " +
                    " WHERE  {  " + subject + " " + predicate + " ?o . ?s ?p ?o }";
            /**
             * Parsing the query string to update the graph
             */
            UpdateAction.parseExecute(queryString, graphStore);
            System.out.println("Got here - deleted triple");
        } catch (Exception e) {
            e.printStackTrace();
            return (false);
        }
        return (true);
    }


    /**
     * Generic RDF positioned update. Deletes and inserts a single given predicate and object for a given subject,
     * without requiring the old value to be known.
     *
     * @param subject
     * @param predicate
     * @param object
     * @return whether the delete is successful
     */
    public boolean positionedUpdateSinglePredicate(String subject, String predicate, String object) {
        try {
            System.out.println("-----positionedUpdateSinglePredicate-------");
            Model model = SDBFactory.connectDefaultModel(store);
            GraphStore graphStore = GraphStoreFactory.create(model);
            String queryString = ontology_ns + "\n" + rdf_ns + "\n" + project_ns + "\n" +
                    //" DELETE {" + subject + " " + predicate + " ?o} " +
                    " DELETE { ?s ?p ?o }" +
                    " WHERE  { " + subject + " " + predicate + " ?o . ?s ?p ?o  }" +
                    " INSERT {" + subject + " " + predicate + " " + object + " } ";


            System.out.println(queryString);
            /**
             * Parsing the query string to update the graph
             */
            UpdateAction.parseExecute(queryString, graphStore);
            System.out.println("Got here - deleted triple");
        } catch (Exception e) {
            e.printStackTrace();
            return (false);
        }
        return (true);
    }


    /* End positioned data methods */

    /**
     * Clears all triples from the default store. USE ADVISEDLY!
     *
     * @return whether the clear operation is successful
     */
    public boolean clearTriples() {
        try {
            Model model = SDBFactory.connectDefaultModel(store);
            GraphStore graphStore = GraphStoreFactory.create(model);

            // For some reason not implemented in ARQ
            //String queryString = "CLEAR";


            String queryString = " DELETE { ?s ?p ?o } WHERE { ?s ?p ?o } ";

            /**
             * Parsing the query string to update the graph
             */
            System.out.println(queryString);
            UpdateAction.parseExecute(queryString, graphStore);
            System.out.println("Got here - cleared all triples");
        } catch (Exception e) {
            e.printStackTrace();
            return (false);
        }
        return (true);
    }


    /* ++++++++++ Create, Update, Retrieve Methods for  Project Object +++++++++++++ */








    /**
     * Generic RDF data query. Retrieves a Map of all predicates and objects
     * associated with a given subject URI.
     *
     * @param queryString
     * @return whether the delete is successful
     */
    public void genericQuery(String queryString) {
        try {
            Model model = SDBFactory.connectDefaultModel(store);
            Query query = QueryFactory.create(queryString);
            QueryExecution queryExect = QueryExecutionFactory.create(query,
                    model);
            try {
                ResultSet result = queryExect.execSelect();
                ResultSetFormatter.out(result);
            } finally {
                queryExect.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
