package models.snapshot;

import com.clarkparsia.empire.annotation.Namespaces;
import com.clarkparsia.empire.annotation.RdfProperty;
import com.clarkparsia.empire.annotation.RdfsClass;
import models.CoSEntityListener;
import models.Indicator;
import models.RdfModel;
import models.Subdomain;

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
public class IndicatorValue extends RdfModel implements Comparable {
    
    @Id
    public Long id;

    @RdfProperty("cos:hasValue")
    protected double value;

    @ManyToOne
    @RdfProperty("cos:hasIndicator")
    protected Indicator indicator;

    @ManyToOne
    @RdfProperty("cos:hasSnapshot")
    protected Snapshot snapshot;

    public IndicatorValue() {}

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

    public Snapshot getSnapshot() {
        return snapshot;
    }

    public void setSnapshot(Snapshot snapshot) {
        this.snapshot = snapshot;
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
