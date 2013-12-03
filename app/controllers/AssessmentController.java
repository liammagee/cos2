package controllers;

import models.*;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;

import java.util.List;
import java.util.Date;
import java.sql.Timestamp;

public class AssessmentController extends Controller {
    static Form<CriticalIssue> issueForm = Form.form(CriticalIssue.class);

    public static Result setupAssessment() {
        return ok(
                views.html.issues.newIssue.render(issueForm)
        );
    }

    public static Result newIssue() {
        Form<CriticalIssue> filledForm = issueForm.bindFromRequest();
        if(filledForm.hasErrors()) {
            return returnToProject();
        } else {
            CriticalIssue issue = filledForm.get();
            Project project = Project.find.byId(Long.valueOf(session("projectId")));
            issue.setProject(project);
            issue.save();
            issue.save();
            return returnToProject();
        }
    }

    public static Result deleteIssue(Long id) {
        CriticalIssue.delete(id);
        return returnToProject();
    }

    public static Result editIssue(Long id) {
        CriticalIssue issue = CriticalIssue.find.byId(id);
        issueForm = Form.form(CriticalIssue.class).fill(
            issue
        );
        return ok(
            views.html.issues.edit.render(id, issueForm)
        );
    }

    public static Result updateIssue(Long id) {
        Form<CriticalIssue> filledForm = Form.form(CriticalIssue.class).bindFromRequest();
        CriticalIssue issue = CriticalIssue.find.byId(id);
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
