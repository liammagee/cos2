package models.snapshot;

import com.clarkparsia.empire.annotation.Namespaces;
import com.clarkparsia.empire.annotation.RdfProperty;
import com.clarkparsia.empire.annotation.RdfsClass;
import models.CoSEntityListener;
import models.RdfModel;
import models.Subdomain;

import javax.persistence.*;
import play.db.ebean.Model;


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
public class AssessmentValue extends Model implements Comparable {
    
    @Id
    public Long id;

    @RdfProperty("cos:hasValue")
    protected double value;

    @OneToOne(fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST})
    @RdfProperty("cos:hasSubdomain")
    protected Subdomain subdomain;

    @ManyToOne
    @RdfProperty("cos:hasAssessment")
    protected Assessment assessment;

    public AssessmentValue() {
    }


    public Long getId() {
        return id;
    }


    public void setId(Long id) {
        this.id = id;
    }
    
    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public Subdomain getSubdomain() {
        return subdomain;
    }

    public void setSubdomain(Subdomain subdomain) {
        this.subdomain = subdomain;
    }


    @Override
    public int compareTo(Object o) {
        AssessmentValue otherValue = (AssessmentValue)o;
        return this.getSubdomain().getParentDomain().getName().compareTo(otherValue.getSubdomain().getParentDomain().getName());
    }

    @Override
    public boolean equals(Object o) {
        return super.equals(o);    //To change body of overridden methods use File | Settings | File Templates.
    }

    @Override
    public int hashCode() {
        return super.hashCode();    //To change body of overridden methods use File | Settings | File Templates.
    }
}
