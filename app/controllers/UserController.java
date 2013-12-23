package controllers;

import models.*;
import play.mvc.Result;
import play.*;
import play.data.Form;
import play.mvc.*;

import init.BasicInit;

import views.html.*;
import views.html.index;

/**
 * Created with IntelliJ IDEA.
 * User: liam
 * Date: 15/11/2013
 * Time: 5:06 PM
 * To change this template use File | Settings | File Templates.
 */
public class UserController extends Controller {
    static Form<User> userForm = Form.form(User.class);


    public static Result showRegister() {
        return ok(
            views.html.users.register.render(userForm)
        );
    }


    public static Result register() {
        Form<User> filledForm = userForm.bindFromRequest();
        if(filledForm.hasErrors()) {
            return badRequest(
                    views.html.users.register.render(filledForm)
            );
        } else {
            User user = filledForm.get();
            User.create(user);
            session("username", user.getUsername());

            // Create the default project - TODO: make this configurable
            BasicInit.addTehranAirPollutionProject(BasicInit.getEntityManager(), user);

            return redirect(routes.ProjectController.projects());
        }
    }

    public static Result login() {
        Form<User> filledForm = userForm.bindFromRequest();
        // Authenticate
        User user = User.authenticate(filledForm.get());
        if(user == null) {
            flash("error", "Sorry, " + filledForm.get().getUsername() + ", you'll need to try logging in again.");
            return unauthorized(
                views.html.index.render(filledForm)
            );
        } else {
            session("username", user.getUsername());
            flash("success", "Welcome " + user.getUsername() + "!");
            return redirect(routes.ProjectController.projects());
        }
    }

    public static Result logout() {
        session().remove("username");
        flash("success", "You have been successfully logged out. ");
        return redirect(routes.Application.index());
    }

    public static String getLoggedInUsername() {
        return session("username");
    }


}
