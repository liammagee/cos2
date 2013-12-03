package models;

//import edu.rmit.sustainability.agentinterface.AgentInterface;
//
//import models.ProgressMonitorRequest;

import javax.persistence.PrePersist;
import javax.persistence.PreRemove;
import javax.persistence.PreUpdate;
import javax.persistence.PostPersist;
import javax.persistence.PostRemove;
import javax.persistence.PostUpdate;
import javax.persistence.PostLoad;


/**
 * Created by IntelliJ IDEA.
 * User: E65691
 * Date: 20/05/11
 * Time: 4:52 PM
 * To change this template use File | Settings | File Templates.
 */
public class CoSEntityListener {



    @PreUpdate
    public void cosPreUpdate(RdfModel model) {
//        System.out.println("Pre-updating " + model.getId());
//    	model.setLastPerformedAction(AbstractEmpireModel.ACT_PREUPDATE);
//        AgentInterface.getInstance().sendData(model.getCurrentProject(), model.getCurrentUser(), model);
//        AgentInterface.getInstance().sendData(model.getCurrentProject(), model.getCurrentUser(), new ProgressMonitorRequest());
    }

    @PreRemove
    public void cosPreRemove(RdfModel model) {
//        System.out.println("Pre-removing " + model.getId());
//    	model.setLastPerformedAction(AbstractEmpireModel.ACT_PREREMOVE);
//        AgentInterface.getInstance().sendData(model.getCurrentProject(), model.getCurrentUser(), model);
//        AgentInterface.getInstance().sendData(model.getCurrentProject(), model.getCurrentUser(), new ProgressMonitorRequest());
    }

    @PrePersist
    public void cosPrePersist(RdfModel model) {
//        System.out.println("Pre-persisting " + model.getId());
//    	model.setLastPerformedAction(AbstractEmpireModel.ACT_PREPERSIST);
//        AgentInterface.getInstance().sendData(model.getCurrentProject(), model.getCurrentUser(), model);
//        AgentInterface.getInstance().sendData(model.getCurrentProject(), model.getCurrentUser(), new ProgressMonitorRequest());
    }

    @PostLoad
    public void cosPostLoad(RdfModel model) {
//        System.out.println("Post-loading " + model.getId());
//    	model.setLastPerformedAction(AbstractEmpireModel.ACT_POSTLOAD);
//        AgentInterface.getInstance().sendData(model.getCurrentProject(), model.getCurrentUser(), model);
//        AgentInterface.getInstance().sendData(model.getCurrentProject(), model.getCurrentUser(), new ProgressMonitorRequest());
    }

    @PostPersist
    public void cosPostPersist(RdfModel model) {
//        System.out.println("Post-persisting " + model.getId());
//    	model.setLastPerformedAction(AbstractEmpireModel.ACT_POSTPERSIST);
//        AgentInterface.getInstance().sendData(model.getCurrentProject(), model.getCurrentUser(), model);
//        AgentInterface.getInstance().sendData(model.getCurrentProject(), model.getCurrentUser(), new ProgressMonitorRequest());
    }

    @PostUpdate
    public void cosPostMerge(RdfModel model) {
//        System.out.println("Post-merging " + model.getId());
//    	model.setLastPerformedAction(AbstractEmpireModel.ACT_POSTMERGE);
//        AgentInterface.getInstance().sendData(model.getCurrentProject(), model.getCurrentUser(), model);
//        AgentInterface.getInstance().sendData(model.getCurrentProject(), model.getCurrentUser(), new ProgressMonitorRequest());
    }

    @PostRemove
    public void cosPostRemoving(RdfModel model) {
//        System.out.println("Post-removing " + model.getId());
//    	model.setLastPerformedAction(AbstractEmpireModel.ACT_POSTREMOVING);
//        AgentInterface.getInstance().sendData(model.getCurrentProject(), model.getCurrentUser(), model);
//        AgentInterface.getInstance().sendData(model.getCurrentProject(), model.getCurrentUser(), new ProgressMonitorRequest());
    }
}
