package models;


import com.clarkparsia.empire.annotation.Namespaces;
import com.clarkparsia.empire.annotation.RdfProperty;
import com.clarkparsia.empire.annotation.RdfsClass;
import models.CoSEntityListener;

import javax.persistence.*;

@Namespaces({"frbr", "http://vocab.org/frbr/core#",
        "dc", "http://purl.org/dc/terms/",
        "cos", "http://circlesofsustainability.org/ontology#",
        "foaf", "http://xmlns.com/foaf/0.1/"})
@RdfsClass("cos:Objective")
@Entity
@EntityListeners(CoSEntityListener.class)
public class Objective extends RdfModel {
    @Id
    public Long id;

    /* A description of the objective */
    @RdfProperty("cos:hasDescription")
    private String description;

    /* Describes whether the objective is to increase the quantity of the underlying issue */
    @RdfProperty("cos:desiredDirection")
    private int desiredDirection;

    /* The underlying issue of the objective */
    @RdfProperty("cos:underlyingIssue")
    private CriticalIssue underlyingIssue;


    public Objective() {
        super();
        description = "";
        desiredDirection = 0;
    }


    public Long getId() {
        return id;
    }


    public void setId(Long id) {
        this.id = id;
    }
    
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public CriticalIssue getUnderlyingIssue() {
        return underlyingIssue;
    }

    public void setUnderlyingIssue(CriticalIssue underlyingIssue) {
        this.underlyingIssue = underlyingIssue;
    }


    public int getDesiredDirection() {
        return desiredDirection;
    }

    public void setDesiredDirection(int desiredDirection) {
        this.desiredDirection = desiredDirection;
    }

}
