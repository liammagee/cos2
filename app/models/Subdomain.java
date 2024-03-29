package models;

import com.clarkparsia.empire.annotation.Namespaces;
import com.clarkparsia.empire.annotation.RdfProperty;
import com.clarkparsia.empire.annotation.RdfsClass;
import models.CoSEntityListener;
import play.db.ebean.Model;

import java.util.*;

import javax.persistence.*;


@Namespaces({"frbr", "http://vocab.org/frbr/core#",
        "dc", "http://purl.org/dc/terms/",
        "cos", "http://circlesofsustainability.org/ontology#",
        "foaf", "http://xmlns.com/foaf/0.1/"})
@RdfsClass("cos:Subdomain")
@Entity
@EntityListeners(CoSEntityListener.class)
public class Subdomain extends RdfModel {
    @Id
    public Long id;

    /* The ID of the subdomain */
    @RdfProperty("cos:subdomainID")
    private int subdomainID;

    /* The name of the subdomain */
    @RdfProperty("cos:hasName")
    private String name;

    /* A description of the subdomain */
    @RdfProperty("cos:hasDescription")
    private String description;

    /* The associated parent domain */
    @ManyToOne(cascade = {CascadeType.ALL})
    @RdfProperty("cos:hasParentDomain")
    private Domain parentDomain;

    /* The associated parent domain */
    @ManyToMany(cascade = {CascadeType.ALL})
    @JoinTable(name = "indicator_subdomain")
    @RdfProperty("cos:hasIndicator")
    private List<Indicator> indicators;


    public Subdomain() {
        indicators = new ArrayList<Indicator>();
    }

    public Subdomain(String name) {
        setName(name);
    }

    public Subdomain(int subdomainID, String name) {
        setSubdomainID(subdomainID);
        setName(name);
    }




    public Long getId() {
        return id;
    }


    public void setId(Long id) {
        this.id = id;
    }

    public int getSubdomainID() {
        return subdomainID;
    }

    public void setSubdomainID(int subdomainID) {
        this.subdomainID = subdomainID;
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

    public Domain getParentDomain() {
        return parentDomain;
    }

    public void setParentDomain(Domain parentDomain) {
        this.parentDomain = parentDomain;
    }

    public List<Indicator> getIndicators() {
        return indicators;
    }

    public void setIndicators(List<Indicator> indicators) {
        this.indicators = indicators;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        Subdomain subdomain = (Subdomain) o;

        if (subdomainID != subdomain.subdomainID) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (description != null ? description.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        StringBuffer buffer = new StringBuffer();
        if (getParentDomain() != null) {
            buffer.append(getParentDomain().getName());
            buffer.append(": ");
        }
        buffer.append(getName());
        return buffer.toString();
    }

    // ORM methods

    public static Model.Finder<Long,Subdomain> find = new Model.Finder(
            Long.class, Subdomain.class
    );

    public static List<Subdomain> all() {
        return find.all();
    }

    public static void create(Subdomain subdomain) {
        subdomain.save();
    }

    public static void update(Subdomain subdomain) {
        subdomain.save();
    }

    public static void delete(Long id) {
        find.ref(id).delete();
    }
}
