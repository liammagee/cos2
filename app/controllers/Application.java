package controllers;

import models.*;
import play.*;
import play.data.Form;
import play.mvc.*;

import views.html.*;
import views.html.index;

public class Application extends Controller {

    public static Result index() {
        return ok(
                views.html.index.render(Form.form(User.class))
        );
        // return redirect(routes.Application.tasks());
    }
}