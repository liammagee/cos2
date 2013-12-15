package controllers;

import models.*;
import models.snapshot.*;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;

import java.util.List;
import java.util.ArrayList;
import java.util.Date;
import java.sql.Timestamp;

public class AssessmentController extends Controller {
    static Form<Assessment> assessmentForm = Form.form(Assessment.class);

    public static Result setupAssessment() {
        Assessment assessment = new Assessment();
        List<Subdomain> subdomains = Subdomain.all();
        List<AssessmentValue> assessmentValues = new ArrayList<AssessmentValue>();

        for (Subdomain subdomain : subdomains) {
            AssessmentValue assessmentValue = new AssessmentValue();
            assessmentValue.setSubdomain(subdomain);
            assessmentValue.setValue(5);
            assessmentValues.add(assessmentValue);
        }
        assessment.setValues(assessmentValues);
        Project project = Project.find.byId(Long.valueOf(session("projectId")));
        assessment.setCreatedAt(new Date());
        assessment.setProject(project);
        assessment.save();
        assessmentForm = Form.form(Assessment.class).fill(assessment);
        return ok(
            views.html.snapshots.edit.render(assessment.id, assessmentForm)
        );
    }


    public static Result deleteAssessment(Long id) {
        Assessment.delete(id);
        return returnToProject();
    }

    public static Result editAssessment(Long id) {
        Assessment assessment = Assessment.find.byId(id);
        assessmentForm = Form.form(Assessment.class).fill(
            assessment
        );
        return ok(
            views.html.snapshots.edit.render(id, assessmentForm)
        );
    }

    public static Result updateAssessment(Long id) {
        Form<Assessment> filledForm = assessmentForm.bindFromRequest();
        if(filledForm.hasErrors()) {
            return editAssessment(id);
        } else {
            Assessment assessment = filledForm.get();
            Assessment underlyingAssessment = Assessment.find.byId(id);
            for (int i = 0; i < assessment.values.size(); i++) {
                AssessmentValue value = assessment.values.get(i);
                AssessmentValue underlyingValue = underlyingAssessment.values.get(i);
                if (value.getValue() != underlyingValue.getValue()) {
                    System.out.println(i);
                    underlyingValue.setValue(value.getValue());
                    underlyingValue.save();
                }
            }
            return returnToProject();
        }
    }

    private static Result returnToProject() {
        return redirect(routes.ProjectController.viewProject(Long.valueOf(session("projectId"))));
    }
}
