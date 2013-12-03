package models.ahp;

import com.clarkparsia.empire.annotation.Namespaces;
import com.clarkparsia.empire.annotation.RdfProperty;
import com.clarkparsia.empire.annotation.RdfsClass;
import models.CoSEntityListener;

import models.RdfModel;

import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.Id;

/**
 * Created by IntelliJ IDEA.
 * CosUser: E65691
 * Date: 24/05/11
 * Time: 10:18 AM
 * To change this template use File | Settings | File Templates.
 */
@Namespaces({"frbr", "http://vocab.org/frbr/core#",
        "dc", "http://purl.org/dc/terms/",
        "cos", "http://circlesofsustainability.org/ontology#",
        "foaf", "http://xmlns.com/foaf/0.1/"})
@RdfsClass("cos:MatrixCell")
@Entity
@EntityListeners(CoSEntityListener.class)
public class MatrixCell extends RdfModel {
    

    @RdfProperty("cos:hasValue")
    protected double value;

    public MatrixCell() {
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }
}
