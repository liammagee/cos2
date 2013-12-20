package models;

import com.clarkparsia.empire.annotation.Namespaces;
import com.clarkparsia.empire.annotation.RdfProperty;
import com.clarkparsia.empire.annotation.RdfsClass;
import play.db.ebean.Model;

import models.snapshot.IndicatorValue;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Namespaces({"frbr", "http://vocab.org/frbr/core#",
        "dc", "http://purl.org/dc/terms/",
        "cos", "http://circlesofsustainability.org/ontology#",
        "foaf", "http://xmlns.com/foaf/0.1/"})
@RdfsClass("cos:Indicator")
@Entity
@EntityListeners(CoSEntityListener.class)
public class Indicator extends RdfModel {
    


    @Id
    public Long id;

    /* The name of the indicator */
    @RdfProperty("cos:hasName")
    private String name;

    /* A description of the indicator */
    @Column(columnDefinition = "TEXT")
    @RdfProperty("cos:hasDescription")
    private String description;

    /* A source for this indicator */
    @RdfProperty("cos:hasSource")
    private String source;

    /* An extenally identifiable code for the indicator */
    @RdfProperty("cos:hasIdentifyingCode")
    private String identifyingCode;

    /* The unit of measure of the indicator */
    @RdfProperty("cos:unitOfMeasure")
    private String unitOfMeasure;

    /* The associated target of this indicator */
    @OneToOne(fetch = FetchType.EAGER, cascade = {CascadeType.ALL})
    @RdfProperty("cos:associatedTarget")
    private Target target;

    /* The set to which this indicator belongs */
    @ManyToOne(cascade = {CascadeType.ALL})
    @RdfProperty("cos:belongsToIndicatorSet")
    public IndicatorSet indicatorSet;

    /* The subordinate indicators, if this indicator is an aggregate. */
    @RdfProperty("cos:subordinateIndicators")
    private List<Indicator> subordinateIndicators;

    /* The parent indicator, if this indicator is subordinate */
    @RdfProperty("cos:parentIndicator")
    private Indicator parentIndicator;

    /* A list of associated issues measured by this indicator */
    @ManyToMany(fetch = FetchType.LAZY,
            cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @RdfProperty("cos:hasIssues")
    private List<CriticalIssue> associatedIssues;

    /* An associated domain of the indicator. */
    @RdfProperty("cos:domain")
    private Domain domain;

    /* An associated subdomain of the indicator. */
    @RdfProperty("cos:subdomain")
    private Subdomain subdomain;


    /* An associated subdomain of the issue. */
    @ManyToMany(fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    @RdfProperty("cos:hasSubdomain")
    private List<Subdomain> subdomains = new ArrayList<Subdomain>();


    /* The creator of the indicator */
    @RdfProperty("cos:createdBy")
    private User creator;

    /* The (optional) project related to this indicator */
    @ManyToOne
    @RdfProperty("cos:hasProject")
    private Project project;

    /* The (optional) project related to this indicator */
    @RdfProperty("cos:hasCategory")
    private String category;

    @OneToMany(fetch = FetchType.EAGER, cascade = {CascadeType.ALL})
    @RdfProperty("cos:hasIndicatorValues")
    public List<IndicatorValue> indicatorValues = new ArrayList<IndicatorValue>();


    /* A series of flags describing what the indicator measures */

    /* Does this indicator measure a social or natural thing? */
    private boolean isSocial;
    private boolean isNatural;

    /* Does this indicator measure a process (over-time) or an object (in-time)? */
    private boolean isProcess;
    private boolean isObject;

    /* Is this indicator aggregate or singular? */
    private boolean isAggregate;
    private boolean isSingular;

    /* Does this indicator measure a spatial or temporal thing? */
    private boolean isSpatial;
    private boolean isTemporal;


    public Indicator() {
        super();
        target = new Target();
        setName("");
        setDescription("");
        setIdentifyingCode("");
        setSource("");
    }



    public Long getId() {
        return id;
    }


    public void setId(Long id) {
        this.id = id;
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


    public String getSource() {
        return source;
    }


    public void setSource(String source) {
        this.source = source;
    }


    public String getIdentifyingCode() {
        return identifyingCode;
    }


    public void setIdentifyingCode(String identifyingCode) {
        this.identifyingCode = identifyingCode;
    }


    public String getUnitOfMeasure() {
        return unitOfMeasure;
    }


    public void setUnitOfMeasure(String unitOfMeasure) {
        this.unitOfMeasure = unitOfMeasure;
    }


    public Target getTarget() {
        return target;
    }


    public void setTarget(Target target) {
        this.target = target;
    }


    public IndicatorSet getIndicatorSet() {
        return indicatorSet;
    }


    public void setIndicatorSet(IndicatorSet indicatorSet) {
        this.indicatorSet = indicatorSet;
    }


    public List<Indicator> getSubordinateIndicators() {
        return subordinateIndicators;
    }


    public void setSubordinateIndicators(List<Indicator> subordinateIndicators) {
        this.subordinateIndicators = subordinateIndicators;
    }


    public Indicator getParentIndicator() {
        return parentIndicator;
    }


    public void setParentIndicator(Indicator parentIndicator) {
        this.parentIndicator = parentIndicator;
    }


    public List<CriticalIssue> getAssociatedIssues() {
        return associatedIssues;
    }


    public void setAssociatedIssues(List<CriticalIssue> associatedIssues) {
        this.associatedIssues = associatedIssues;
    }

    
    public List<IndicatorValue> getIndicatorValues() {
        return indicatorValues;
    }

    public void setIndicatorValues(List<IndicatorValue> indicatorValues) {
        this.indicatorValues = indicatorValues;
    }



    public Domain getDomain() {
        return domain;
    }


    public void setDomain(Domain domain) {
        this.domain = domain;
    }


    public Subdomain getSubdomain() {
        return subdomain;
    }

    public List<Subdomain> getSubdomains() {
        return subdomains;
    }

    public void setSubdomains(List<Subdomain> subdomains) {
        this.subdomains = subdomains;
    }


    public User getCreator() {
        return creator;
    }

    public void setCreator(User creator) {
        this.creator = creator;
    }

    public void setSubdomain(Subdomain subdomain) {
        this.subdomain = subdomain;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }


    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }


    public boolean isSocial() {
        return isSocial;
    }


    public void setSocial(boolean isSocial) {
        this.isSocial = isSocial;
    }


    public boolean isNatural() {
        return isNatural;
    }


    public void setNatural(boolean isNatural) {
        this.isNatural = isNatural;
    }


    public boolean isProcess() {
        return isProcess;
    }


    public void setProcess(boolean isProcess) {
        this.isProcess = isProcess;
    }


    public boolean isObject() {
        return isObject;
    }


    public void setObject(boolean isObject) {
        this.isObject = isObject;
    }


    public boolean isAggregate() {
        return isAggregate;
    }


    public void setAggregate(boolean isAggregate) {
        this.isAggregate = isAggregate;
    }


    public boolean isSingular() {
        return isSingular;
    }


    public void setSingular(boolean isSingular) {
        this.isSingular = isSingular;
    }


    public boolean isSpatial() {
        return isSpatial;
    }


    public void setSpatial(boolean isSpatial) {
        this.isSpatial = isSpatial;
    }


    public boolean isTemporal() {
        return isTemporal;
    }


    public void setTemporal(boolean isTemporal) {
        this.isTemporal = isTemporal;
    }

    public boolean equals(Indicator indicator) {
        if (indicator.id.equals(this.id))
            return (true);
        return (false);
    }

    public String toString() {

        String str = "*** INDICATOR: " + getName() + " ***\n";
        str += "Description: " + getDescription() + "\n";
        str += "Source: " + getSource() + "\n";
        return (str);
    }

    // ORM methods  

    public static Model.Finder<Long,Indicator> find = new Model.Finder(
            Long.class, Indicator.class
    );

    public static List<Indicator> all() {
        return find.all();
    }

    public static void create(Indicator indicator) {
        indicator.save();
    }

    public static void update(Indicator indicator) {
        indicator.save();
    }

    public static void delete(Long id) {
        find.ref(id).delete();
    }

}
