package controllers;

import models.*;
import models.snapshot.*;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;

import java.util.List;
import java.util.Date;
import java.sql.Timestamp;

public class AssessmentController extends Controller {
    static Form<Assessment> assessmentForm = Form.form(Assessment.class);

    public static Result setupAssessment() {
        return ok(
                views.html.snapshots.newAssessment.render(assessmentForm)
        );
    }

    public static Result newAssessment() {
        Form<Assessment> filledForm = assessmentForm.bindFromRequest();
        if(filledForm.hasErrors()) {
            return returnToProject();
        } else {
            Assessment assessment = filledForm.get();
            Project project = Project.find.byId(Long.valueOf(session("projectId")));
            assessment.setProject(project);
            assessment.save();
            return returnToProject();
        }
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
        Form<Assessment> filledForm = Form.form(Assessment.class).bindFromRequest();
        Assessment assessment = Assessment.find.byId(id);
        if(filledForm.hasErrors()) {
            return returnToProject();
        } else {
            filledForm.get().update(id);
            return returnToProject();
        }
    }

    private static Result returnToProject() {
        return redirect(routes.ProjectController.viewProject(Long.valueOf(session("projectId"))));
    }
}
