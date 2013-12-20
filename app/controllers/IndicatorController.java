package controllers;

import models.*;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;

import java.util.List;
import java.util.ArrayList;
import java.util.Date;
import java.sql.Timestamp;

/**
 * Created with IntelliJ IDEA.
 * User: liam
 * Date: 15/11/2013
 * Time: 5:06 PM
 * To change this template use File | Settings | File Templates.
 */
public class IndicatorController extends Controller {
    static Form<Indicator> indicatorForm = Form.form(Indicator.class);

    public static Result setupIndicator(Long issueId) {
        List<Subdomain> subdomains = Subdomain.find.where().orderBy("id").findList();
        return ok(
                views.html.indicators.newIndicator.render(issueId, indicatorForm, subdomains)
        );
    }

    public static Result newIndicator(Long issueId) {
        Form<Indicator> filledForm = indicatorForm.bindFromRequest();
        if(filledForm.hasErrors()) {
            return returnToIssue(issueId);
        } else {
            Indicator indicator = filledForm.get();
            CriticalIssue issue = CriticalIssue.find.byId(issueId);
            indicator.setProject(ProjectController.getProject());
            List<CriticalIssue> issues = indicator.getAssociatedIssues();
            if (issues == null)
                issues = new ArrayList();
            issues.add(issue);
            List<Subdomain> newSubdomains = new ArrayList<Subdomain>();
            for (Subdomain subdomain : indicator.getSubdomains()) {
                if (subdomain != null && subdomain.id != null) {
                    Subdomain underlyingSubdomain = Subdomain.find.byId(subdomain.id);
                    newSubdomains.add(underlyingSubdomain);
                }
            }
            indicator.setSubdomains(newSubdomains);
            indicator.save();
            return returnToIssue(issueId);
        }
    }

    public static Result deleteIndicator(Long issueId, Long id) {
        CriticalIssue issue = CriticalIssue.find.byId(issueId);
        Indicator indicator = Indicator.find.byId(id);
        indicator.getAssociatedIssues().remove(issue);
        indicator.getAssociatedIssues().remove(issue);
        indicator.save();    
        if (indicator.getIndicatorSet() != null) {
            Indicator.delete(id);
        }
        return returnToProject();
    }

    public static Result editIndicator(Long issueId, Long id) {
        Indicator indicator = Indicator.find.byId(id);
        List<Subdomain> subdomains = Subdomain.find.where().orderBy("id").findList();
        indicatorForm = Form.form(Indicator.class).fill(
            indicator
        );
        return ok(
            views.html.indicators.edit.render(issueId, id, indicatorForm, subdomains)
        );
    }

    public static Result updateIndicator(Long issueId, Long id) {
        Form<Indicator> filledForm = Form.form(Indicator.class).bindFromRequest();
        Indicator indicator = Indicator.find.byId(id);
        if(filledForm.hasErrors()) {
            return returnToIssue(issueId);
        } else {
            Indicator newIndicator = filledForm.get();
            List<Subdomain> newSubdomains = new ArrayList<Subdomain>();
            for (Subdomain subdomain : newIndicator.getSubdomains()) {
                if (subdomain != null && subdomain.id != null) {
                    Subdomain underlyingSubdomain = Subdomain.find.byId(subdomain.id);
                    newSubdomains.add(underlyingSubdomain);
                }
            }
            newIndicator.setSubdomains(newSubdomains);
            newIndicator.update(id);
            return returnToProject();
        }
    }

    private static Result returnToIssue(Long issueId) {
        return redirect(routes.IssueController.editIssue(issueId));
    }

    private static Result returnToProject() {
        return redirect(routes.ProjectController.viewProject(Long.valueOf(session("projectId"))));
    }
}
