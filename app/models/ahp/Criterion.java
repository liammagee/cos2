package models.ahp;

import com.clarkparsia.empire.annotation.Namespaces;
import com.clarkparsia.empire.annotation.RdfProperty;
import com.clarkparsia.empire.annotation.RdfsClass;
import models.CoSEntityListener;
import models.RdfModel;

import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.Id;

@Namespaces({"frbr", "http://vocab.org/frbr/core#",
        "dc", "http://purl.org/dc/terms/",
        "cos", "http://circlesofsustainability.org/ontology#",
        "foaf", "http://xmlns.com/foaf/0.1/"})
@RdfsClass("cos:Criterion")
@Entity
@EntityListeners(CoSEntityListener.class)
public class Criterion extends RdfModel {
    @Id
    public Integer id;

    /* The name of the criterion */
    @RdfProperty("cos:hasName")
    private String name;

    /* A description of the criterion */
    @RdfProperty("cos:hasDescription")
    private String description;

    /* The initial weight for this criterion */
    @RdfProperty("cos:hasInitialWeight")
    private int initialWeight;


    public Criterion() {
    }

    public Criterion(String name, String description, int initialWeight) {
        this.name = name;
        this.description = description;
        this.initialWeight = initialWeight;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getInitialWeight() {
        return initialWeight;
    }

    public void setInitialWeight(int initialWeight) {
        this.initialWeight = initialWeight;
    }
}
