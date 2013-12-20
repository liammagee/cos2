/**
 *
 */
package models;

import javax.persistence.*;
import java.util.*;

import com.clarkparsia.empire.annotation.Namespaces;
import com.clarkparsia.empire.annotation.RdfProperty;
import com.clarkparsia.empire.annotation.RdfsClass;
import models.snapshot.Assessment;
import org.json.simple.JSONObject;
import play.db.ebean.Model;

import models.ahp.*;


/**
 * @author Liam
 */
@Namespaces({"frbr", "http://vocab.org/frbr/core#",
        "dc", "http://purl.org/dc/terms/",
        "cos", "http://circlesofsustainability.org/ontology#",
        "foaf", "http://xmlns.com/foaf/0.1/"})
@RdfsClass("cos:Project")
@Entity
@EntityListeners(CoSEntityListener.class)
public class Project extends RdfModel {

    @Id
    public Long id;

    @RdfProperty("cos:hasName")
    public String projectName;

    @Column(columnDefinition = "TEXT")
    @RdfProperty("cos:hasDescription")
    public String projectDescription;

    @RdfProperty("cos:hasGeneralDescription")
    public String generalIssue;

    @RdfProperty("cos:hasNormativeGoal")
    public String normativeGoal;

    @RdfProperty("cos:createdAt")
    public Date createdAt;

    @RdfProperty("cos:startDate")
    public Date startDate;

    @RdfProperty("cos:endDate")
    public Date endDate;

    @RdfProperty("cos:city")
    public String city;

    @RdfProperty("cos:visibility")
    public Integer visibility;

    @RdfProperty("cos:publishedState")
    public Integer publishedState;


    @ManyToOne(fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @RdfProperty("cos:createdBy")
    public User creator;

    @OneToOne(fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    @RdfProperty("cos:projectProgress")
    public ProjectProgress projectProgress;


    // @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    // @RdfProperty("cos:hasCollaborator")
    private List<User> collaborators = new ArrayList<User>();

    @OneToMany(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE})
    @OrderBy("name ASC")
    @RdfProperty("cos:hasCriticalIssue")
    private List<CriticalIssue> criticalIssues = new ArrayList<CriticalIssue>();

    @OneToMany(fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE})
    @OrderBy("name ASC")
    @RdfProperty("cos:hasIndicator")
    private List<Indicator> indicators = new ArrayList<Indicator>();


    @OneToOne(fetch = FetchType.LAZY, cascade = {CascadeType.ALL})
    @RdfProperty("cos:hasAHP")
    public AHP ahp;

    @OneToMany(fetch = FetchType.LAZY, cascade = {CascadeType.ALL})
    @RdfProperty("cos:hasAssessments")
    private List<Assessment> assessments = new ArrayList<Assessment>();


    public Project() {
        this.projectName = projectName;
        this.projectDescription = "";
        this.generalIssue = "";
        this.normativeGoal = "";
        this.visibility = new Integer(0);
        this.publishedState = new Integer(0);
    }



    public Long getId() {
        return id;
    }


    public void setId(Long id) {
        this.id = id;
    }


    public String getProjectName() {
        return projectName;
    }


    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }


    public String getProjectDescription() {
        return projectDescription;
    }


    public void setProjectDescription(String projectDescription) {
        this.projectDescription = projectDescription;
    }


    public String getGeneralIssue() {
        return generalIssue;
    }


    public void setGeneralIssue(String generalIssue) {
        this.generalIssue = generalIssue;
    }


    public String getNormativeGoal() {
        return normativeGoal;
    }


    public void setNormativeGoal(String normativeGoal) {
        this.normativeGoal = normativeGoal;
    }



    public void setCriticalIssues(List<CriticalIssue> ciList) {
        criticalIssues = ciList;
    }


    public List<CriticalIssue> getCriticalIssues() {
        return (criticalIssues);
    }

    public List<Indicator> getIndicators() {
        return indicators;
    }

    public void setIndicators(List<Indicator> indicators) {
        this.indicators = indicators;
    }

    public List<Assessment> getAssessments() {
        return assessments;
    }

    public void setAssessments(List<Assessment> assessments) {
        this.assessments = assessments;
    }

    /**
     * Generates a map of ranks and their associated issues, sorted from highest to lowest ranked.
     * @return a simple, score-based ranking of issues
     */
    public Map<Integer, List<CriticalIssue>> getSimpleCriticalIssueRanking() {
        Comparator<CriticalIssue> comparator = new Comparator<CriticalIssue>() {
            public int compare(CriticalIssue criticalIssue1, CriticalIssue criticalIssue2) {
                return criticalIssue2.getPerceivedSignificance() - criticalIssue1.getPerceivedSignificance();
            }
        };
        Collections.sort(criticalIssues, comparator);
        Map<Integer, List<CriticalIssue>> ranking = new TreeMap<Integer, List<CriticalIssue>>();
        if (criticalIssues.size() > 0) {
            int previousSignificance = criticalIssues.get(0).getPerceivedSignificance();
            int currentSignificance = 0;
            int rank = 1;
            int counter = 0;
            List<CriticalIssue> equivalentlyRankedIssues = new ArrayList<CriticalIssue>();

            // Rank issues: from those with highest perceived significance down
            for (CriticalIssue criticalIssue : criticalIssues) {
                currentSignificance = criticalIssue.getPerceivedSignificance();
                if (currentSignificance < previousSignificance) {
                    ranking.put(rank, equivalentlyRankedIssues);
                    equivalentlyRankedIssues = new ArrayList<CriticalIssue>();
                    rank += counter;
                    counter = 1;
                }
                else
                    counter++;
                equivalentlyRankedIssues.add(criticalIssue);
                previousSignificance = currentSignificance;
            }
            // Put last entry in now
            ranking.put(rank, equivalentlyRankedIssues);

        }
        return ranking;
    }






    /**
     * Adds a critical issue to the current collection
     * @param criticalIssue
     */
    public void addCriticalIssue(CriticalIssue criticalIssue) {
        criticalIssues.add(criticalIssue);
    }

    public User getCreator() {
        return creator;
    }

    public void setCreator(User creator) {
        this.creator = creator;
    }

    public List<User> getCollaborators() {
        return collaborators;
    }

    public void setCollaborators(List<User> collaborators) {
        this.collaborators = collaborators;
    }

    public void addCollaborator(User collaborator) {
        if (this.collaborators == null)
            this.collaborators = new ArrayList<User>();
        this.collaborators.add(collaborator);
    }

    public ProjectProgress getProjectProgress() {
        return projectProgress;
    }

    public void setProjectProgress(ProjectProgress projectProgress) {
        this.projectProgress = projectProgress;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public Integer getVisibility() {
        return visibility;
    }

    public void setVisibility(Integer visibility) {
        this.visibility = visibility;
    }

    public Integer getPublishedState() {
        return publishedState;
    }

    public void setPublishedState(Integer publishedState) {
        this.publishedState = publishedState;
    }

    public void updateProgress() {
        if (projectProgress == null)
            projectProgress = new ProjectProgress();
        projectProgress.setProjectCreated(1);
        if (generalIssue != null && !generalIssue.equals(""))
            projectProgress.setGeneralIssueDefined(1);
        else
            projectProgress.setGeneralIssueDefined(0);
        if (normativeGoal != null && !normativeGoal.equals(""))
            projectProgress.setNormativeGoalDefined(1);
        else
            projectProgress.setNormativeGoalDefined(0);
        if (assessments.size() > 0)
            projectProgress.setHasInitialAssessmentBeenConducted(1);
        else
            projectProgress.setHasInitialAssessmentBeenConducted(0);
        if (criticalIssues.size() > 3)
            projectProgress.setDoAtLeastFourIssuesExist(1);
        else
            projectProgress.setDoAtLeastFourIssuesExist(0);
        if (indicators.size() > 3)
            projectProgress.setDoAtLeastFourIndicatorsExist(1);
        else
            projectProgress.setDoAtLeastFourIndicatorsExist(0);

        List<Domain> coveredDomains = new ArrayList<Domain>();
        boolean hasIndicators = true;
        for (CriticalIssue issue : criticalIssues) {
            if (!coveredDomains.contains(issue.getDomain()))
                coveredDomains.add(issue.getDomain());
            if (issue.getIndicators().size() == 0)
                hasIndicators = false;
        }
        if (coveredDomains.size() > 3)
            projectProgress.setAreAllDomainsCoveredByIssues(1);
        else
            projectProgress.setAreAllDomainsCoveredByIssues(0);

        if (hasIndicators)
            projectProgress.setAreAllIssuesMeasuredByAtLeastOneIndicator(1);
        else
            projectProgress.setAreAllIssuesMeasuredByAtLeastOneIndicator(0);

        projectProgress.save();
    }


    public String toString() {
        String str = "********* PROJECT " + getProjectName() + " **********\n";
        str += "Description: " + getProjectDescription() + "\n";
        str += "General Issue: " + getGeneralIssue() + "\n";
        str += "Normative Goal: " + getNormativeGoal() + "\n";
        str += "--------------------------------------------------\n";

        String ciNames = "";
        String ciFull = "";

        List<CriticalIssue> ciList = getCriticalIssues();
        if (ciList != null)
            for (CriticalIssue ci : ciList) {
                ciNames += "- " + ci.getName() + "\n";
                ciFull += ci.toString();
            }
        str += "Summary of Critical Issues:\n";
        str += ciNames;
        str += "--------------------------------------------------\n";
        str += ciFull;
        str += "--------------------------------------------------\n";

        return (str);
    }

    public String toJSONString(){
        JSONObject obj = new JSONObject();
        obj.put("projectName", projectName);
        obj.put("projectDescription", projectDescription);
        if (createdAt != null)
            obj.put("createdAt", createdAt.toString());
        if (creator != null)
            obj.put("creator", creator.getUsername());
        obj.put("projectProgress", projectProgress);
        return obj.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Project project = (Project) o;

        if (generalIssue != null ? !generalIssue.equals(project.generalIssue) : project.generalIssue != null)
            return false;
        if (normativeGoal != null ? !normativeGoal.equals(project.normativeGoal) : project.normativeGoal != null)
            return false;
        if (projectDescription != null ? !projectDescription.equals(project.projectDescription) : project.projectDescription != null)
            return false;
        if (projectName != null ? !projectName.equals(project.projectName) : project.projectName != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (projectName != null ? projectName.hashCode() : 0);
        result = 31 * result + (projectDescription != null ? projectDescription.hashCode() : 0);
        result = 31 * result + (generalIssue != null ? generalIssue.hashCode() : 0);
        result = 31 * result + (normativeGoal != null ? normativeGoal.hashCode() : 0);
        return result;
    }



    // ORM methods

    public static Model.Finder<Long,Project> find = new Model.Finder(
            Long.class, Project.class
    );

    public static List<Project> all() {
        return find.all();
    }

    public static void create(Project project) {
        project.save();
    }

    public static void update(Project project) {
        project.save();
    }

    public static void delete(Long id) {
        find.ref(id).delete();
    }


}

