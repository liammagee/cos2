package models.snapshot;


import com.clarkparsia.empire.annotation.Namespaces;
import com.clarkparsia.empire.annotation.RdfProperty;
import com.clarkparsia.empire.annotation.RdfsClass;
import models.CoSEntityListener;
import models.User;
import models.Project;
import models.RdfModel;
import play.db.ebean.Model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author Liam Magee
 * @since 25/03/2011
 */
@Namespaces({"frbr", "http://vocab.org/frbr/core#",
        "dc", "http://purl.org/dc/terms/",
        "cos", "http://circlesofsustainability.org/ontology#",
        "foaf", "http://xmlns.com/foaf/0.1/"})
@RdfsClass("cos:Snapshot")
@Entity
@EntityListeners(CoSEntityListener.class)
public class Snapshot extends RdfModel {
    
    @Id
    public Long id;

    /* The date of the assessment */
    @RdfProperty("cos:createdAt")
    private Date createdAt;

    @ManyToOne(fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @RdfProperty("cos:createdBy")
    public User creator;

    /* The project related to this assessment */
    @ManyToOne(cascade = CascadeType.ALL)
    @RdfProperty("cos:hasProject")
    public Project project;

    @OneToMany(fetch = FetchType.EAGER, cascade = {CascadeType.ALL})
    @RdfProperty("cos:hasAssessmentValues")
    public List<AssessmentValue> assessmentValues = new ArrayList<AssessmentValue>();


    @OneToOne(fetch = FetchType.EAGER, cascade = {CascadeType.ALL})
    @RdfProperty("cos:hasAssessment")
    public Assessment assessment;


    @OneToMany(fetch = FetchType.EAGER, cascade = {CascadeType.ALL})
    @RdfProperty("cos:hasIndicatorValues")
    public List<IndicatorValue> indicatorValues = new ArrayList<IndicatorValue>();


    public Snapshot() {
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
}

    public User getCreator() {
        return creator;
    }

    public void setCreator(User creator) {
        this.creator = creator;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public Assessment getAssessment() {
        return assessment;
    }

    public void setAssessment(Assessment assessment) {
        this.assessment = assessment;
    }

    public List<AssessmentValue> getAssessmentValues() {
        return assessmentValues;
    }

    public void setAssessmentValues(List<AssessmentValue> assessmentValues) {
        this.assessmentValues = assessmentValues;
    }

    public List<IndicatorValue> getIndicatorValues() {
        return indicatorValues;
    }

    public void setIndicatorValues(List<IndicatorValue> indicatorValues) {
        this.indicatorValues = indicatorValues;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        Snapshot that = (Snapshot) o;

        if (createdAt != null ? !createdAt.equals(that.createdAt) : that.createdAt != null) return false;
        if (creator != null ? !creator.equals(that.creator) : that.creator != null) return false;
        if (project != null ? !project.equals(that.project) : that.project != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        // If we have a RDF ID property set, defer to the superclass implementation
        if (mIdSupport.getRdfId() != null)
            return super.hashCode();

        int result = super.hashCode();
        result = 31 * result + (createdAt != null ? createdAt.hashCode() : 0);
        result = 31 * result + (creator != null ? creator.hashCode() : 0);
        result = 31 * result + (project != null ? project.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Snapshot{" +
                "createdAt=" + createdAt +
                '}';
    }


    // ORM methods  

    public static Model.Finder<Long,Snapshot> find = new Model.Finder(
            Long.class, Snapshot.class
    );

    public static List<Snapshot> all() {
        return find.all();
    }

    public static void create(Snapshot snapshot) {
        snapshot.save();
    }

    public static void update(Snapshot snapshot) {
        snapshot.save();
    }

    public static void delete(Long id) {
        find.ref(id).delete();
    }
}


