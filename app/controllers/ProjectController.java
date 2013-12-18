package controllers;

import models.*;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;

import java.util.List;
import java.util.Date;
import java.sql.Timestamp;

/**
 * Created with IntelliJ IDEA.
 * User: liam
 * Date: 15/11/2013
 * Time: 5:06 PM
 * To change this template use File | Settings | File Templates.
 */
public class ProjectController extends Controller {
    static Form<Project> projectForm = Form.form(Project.class);


    public static Result projects() {
        return ok(
            views.html.projects.projectList.render(Project.all(), projectForm)
        );
    }


    public static Result newProject() {
        Form<Project> filledForm = projectForm.bindFromRequest();
        if(filledForm.hasErrors()) {
            return badRequest(
                    views.html.projects.projectList.render(Project.all(), filledForm)
            );
        } else {
            Project project = filledForm.get();
            project.projectProgress = new models.ProjectProgress();
            project.creator = User.findByUsername(session("username"));
            project.createdAt = new Date();
            Date date = new Date();
            project.createdAt = new Timestamp(date.getTime());
            project.save();
            return redirect(routes.ProjectController.projects());
        }
    }

    public static Result deleteProject(Long id) {
        Project.delete(id);
        return redirect(routes.ProjectController.projects());
    }

    public static Result viewProject(Long id) {
        session("projectId", id.toString());
        Project project = Project.find.byId(id);
        projectForm = Form.form(Project.class).fill(
            project
        );

        // Update the project status
        project.updateProgress();

        List<IndicatorSet> indicatorSets = IndicatorSet.all();
        return ok(
            views.html.projects.projectView.render(id, projectForm, indicatorSets)
        );
    }

    public static Result reportProject(Long id) {
        Project project = Project.find.byId(id);
        projectForm = Form.form(Project.class).fill(
            project
        );
        return ok(
            views.html.projects.projectReport.render(id, projectForm)
        );
    }

    public static Result editProject(Long id) {
        Project project = Project.find.byId(id);
        projectForm = Form.form(Project.class).fill(
            project
        );
        return ok(
                views.html.projects.projectEdit.render(id, projectForm)
        );
    }

    public static Result updateProject(Long id) {
        Form<Project> filledForm = Form.form(Project.class).bindFromRequest();
        if(filledForm.hasErrors()) {
            return badRequest(
                    views.html.projects.projectList.render(Project.all(), filledForm)
            );
        } else {
            filledForm.get().update(id);
            flash("success", "Project " + filledForm.get().projectName + " has been updated");
            return redirect(routes.ProjectController.viewProject(id));
        }
    }

    public static Project getProject() {
        return Project.find.byId(Long.valueOf(session("projectId")));
    }

    public static Result returnToProject() {
        return redirect(routes.ProjectController.viewProject(Long.valueOf(session("projectId"))));
    }

}
