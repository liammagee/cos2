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
public class InfoController extends Controller {

    public static Result about() {
        return ok(
            views.html.info.about.render()
        );
    }
}
