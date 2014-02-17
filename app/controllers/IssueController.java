package controllers;

import models.*;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;

import java.util.List;
import java.util.ArrayList;
import java.util.Date;
import java.sql.Timestamp;

public class IssueController extends Controller {
    static Form<CriticalIssue> issueForm = Form.form(CriticalIssue.class);

    public static Result setupIssue() {
        List<Subdomain> subdomains = Subdomain.all();
        return ok(
            views.html.issues.newIssue.render(issueForm, subdomains)
        );
    }

    public static Result newIssue() {
        Form<CriticalIssue> filledForm = Form.form(CriticalIssue.class).bindFromRequest();
        if(filledForm.hasErrors()) {
            return returnToProject();
        } else {
            CriticalIssue issue = filledForm.get();
            Project project = Project.find.byId(Long.valueOf(session("projectId")));
            issue.setProject(project);
            List<Subdomain> newSubdomains = new ArrayList<Subdomain>();
            for (Subdomain subdomain : issue.getSubdomains()) {
                if (subdomain != null && subdomain.id != null) {
                    Subdomain underlyingSubdomain = Subdomain.find.byId(subdomain.id);
                    newSubdomains.add(underlyingSubdomain);
                }
            }
            issue.setSubdomains(newSubdomains);
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
        List<Subdomain> subdomains = Subdomain.all();
        issueForm = Form.form(CriticalIssue.class).fill(
            issue
        );
        return ok(
            views.html.issues.edit.render(id, issueForm, subdomains)
        );
    }

    public static Result updateIssue(Long id) {
        Form<CriticalIssue> filledForm = Form.form(CriticalIssue.class).bindFromRequest();
        CriticalIssue issue = CriticalIssue.find.byId(id);
        if(filledForm.hasErrors()) {
            return returnToProject();
        } else {
            CriticalIssue foundIssue = filledForm.get();
            foundIssue.setIndicators(issue.getIndicators());
            List<Subdomain> newSubdomains = new ArrayList<Subdomain>();
            for (Subdomain subdomain : foundIssue.getSubdomains()) {
                if (subdomain != null && subdomain.id != null) {
                    Subdomain underlyingSubdomain = Subdomain.find.byId(subdomain.id);
                    newSubdomains.add(underlyingSubdomain);
                }
            }
            foundIssue.setSubdomains(newSubdomains);
            foundIssue.update(id);
            return returnToProject();
        }
    }

    private static Result returnToProject() {
        return redirect(routes.ProjectController.viewProject(Long.valueOf(session("projectId"))));
    }

    public static Long getIssue() {
        try {
            return new Long(session("issueId"));
        }
        catch (NumberFormatException nfe) {
            return null;
        }
    }

    public static String getIssueName() {
        if (getIssue() != null) {
            CriticalIssue issue = CriticalIssue.find.byId(getIssue());
            return issue.getName();
        }
        return null;
    }
}
