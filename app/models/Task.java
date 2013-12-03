package models;

import java.net.URI;
import java.util.*;

import com.clarkparsia.empire.SupportsRdfId;
import com.clarkparsia.empire.annotation.Namespaces;
import com.clarkparsia.empire.annotation.RdfProperty;
import com.clarkparsia.empire.annotation.RdfsClass;
import play.db.ebean.*;
import play.data.validation.Constraints.*;

import javax.persistence.*;

@Namespaces({"frbr", "http://vocab.org/frbr/core#",
        "dc", "http://purl.org/dc/terms/",
        "cos", "http://circlesofsustainability.org/ontology#",
        "foaf", "http://xmlns.com/foaf/0.1/"})
@RdfsClass("cos:Task")
@Entity
public class Task extends models.RdfModel  {

    /**
     * Default support for managing and accessing the rdf:ID of this object
     */


    @RdfProperty("cos:hasLabel")
    @Required
    public String label;

    public static Model.Finder<Long,Task> find = new Model.Finder(
            Long.class, Task.class
    );

    public static List<Task> all() {
        return find.all();
    }

    public static void create(Task task) {
        task.save();
    }

    public static void delete(Long id) {
        find.ref(id).delete();
    }

}