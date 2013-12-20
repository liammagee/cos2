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
public class IndicatorSetController extends Controller {
    static Form<Indicator> indicatorForm = Form.form(Indicator.class);


    public static Result viewAllIndicatorSets() {
        List<IndicatorSet> indicatorSets = IndicatorSet.all();
        return ok(
            views.html.indicatorsets.indicatorSetView.render(indicatorSets)
        );
    }

    public static Result viewIndicatorSet(Long id) {
        IndicatorSet indicatorSet = IndicatorSet.find.byId(id);
        List<IndicatorSet> indicatorSets = new ArrayList<IndicatorSet>();
        indicatorSets.add(indicatorSet);
        return ok(
            views.html.indicatorsets.indicatorSetView.render(indicatorSets)
        );
    }

    public static Result chooseIndicator(Long id) {
        /*
        Form<Indicator> filledForm = Form.form(Indicator.class).bindFromRequest();
        Indicator indicator = Indicator.find.byId(id);
        if(filledForm.hasErrors()) {
            return returnToIssue(issueId);
        } else {
            filledForm.get().update(id);
            return returnToProject();
        }
        */
        return ProjectController.returnToProject();
    }

}
