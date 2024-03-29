package models.snapshot;

import com.clarkparsia.empire.annotation.Namespaces;
import com.clarkparsia.empire.annotation.RdfProperty;
import com.clarkparsia.empire.annotation.RdfsClass;
import models.CoSEntityListener;
import models.Indicator;
import models.RdfModel;
import models.Subdomain;

import play.db.ebean.Model;

import javax.persistence.*;

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
@RdfsClass("cos:IndicatorValue")
@Entity
@EntityListeners(CoSEntityListener.class)
public class IndicatorValue extends Model implements Comparable {
    
    @Id
    public Long id;

    @RdfProperty("cos:hasValue")
    protected double value;

    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE})
    @RdfProperty("cos:hasIndicator")
    protected Indicator indicator;

    @ManyToOne
    @RdfProperty("cos:hasAssessment")
    protected Assessment assessment;

    public IndicatorValue() {}


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

    public Indicator getIndicator() {
        return indicator;
    }

    public void setIndicator(Indicator indicator) {
        this.indicator = indicator;
    }

    public Assessment getAssessment() {
        return assessment;
    }

    public void setAssessment(Assessment assessment) {
        this.assessment = assessment;
    }

    @Override
    public int compareTo(Object o) {
        IndicatorValue otherValue = (IndicatorValue)o;
        return this.getIndicator().getName().compareTo(otherValue.getIndicator().getName());
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
