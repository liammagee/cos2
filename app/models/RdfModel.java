/*
 * Copyright (c) 2009-2011 Clark & Parsia, LLC. <http://www.clarkparsia.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package models;

import com.clarkparsia.empire.SupportsRdfId;
import com.clarkparsia.empire.annotation.SupportsRdfIdImpl;
import com.clarkparsia.play.imperium.Imperium;

import play.db.ebean.Model;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

import java.net.URI;

/**
 * <p>Extends the normal Play JPA based model with hooks to commit changes to this object to an RDF-based
 * data source in addition to the normal SQL-backed database.</p>
 *
 * @author Michael Grove
 * @since 0.1
 */
@MappedSuperclass
@Entity
public class RdfModel extends Model implements SupportsRdfId {


    @Id
    public Long id;

    /**
     * Default support for managing and accessing the rdf:ID of this object
     */
    //@Column(name = "rdfid", nullable = false, unique=true, updatable=false)
    public String rdfid;

    public RdfModel() {}

    /**
     * @inheritDoc
     */
    @SuppressWarnings("unchecked")
    @Override
//  public <T extends JPASupport> T save() {
    public void save() {
        try {
            if (Imperium.em().contains(this)) {
                Imperium.em().merge(this);
            }
            else {
                Imperium.em().persist(this);
            }
        }
        catch (Throwable theException) {
            theException.printStackTrace();
            // the changes to the underlying database failed, we need to schedule a job to synch the hibernate
            // database and the rdf database at a later date.
//          SyncJob.schedule();
        }

        super.save();
    }

    @Deprecated // exposed only to allow the sarge backwards migration.
    public void superSave() {
        super.save();
    }

    /**
     * @inheritDoc
     */
    @SuppressWarnings("unchecked")
    @Override
    public void delete() {
        try {
            Imperium.em().remove(this);
        }
        catch (Throwable theException) {
            // the changes to the underlying database failed, we need to schedule a job to synch the hibernate
            // database and the rdf database at a later date.
//          SyncJob.schedule();
        }

        super.delete();
    }

    /**
     * @inheritDoc
     */
    public SupportsRdfId.RdfKey getRdfId() {
        return rdfid == null ? null : new SupportsRdfId.URIKey(URI.create(rdfid));
    }


    /**
     * @inheritDoc
     */
    public void setRdfId(final SupportsRdfId.RdfKey theId) {
        if (theId == null) {
            rdfid = null;
        }
        else {
            rdfid = theId.toString();
        }
    }




    /**
     * Default support for the ID of an RDF concept
     */
    protected SupportsRdfIdImpl mIdSupport = new SupportsRdfIdImpl();

    /**
     * Provides context support, so the associated project
     */
    protected transient Project currentProject;

    /**
     * Provides context support, so the action performed on this object (add, delete, etc...)
     */
    protected transient int lastPerformedAction;


    public String getId() {
        return mIdSupport.getRdfId().value().toString();
    }

    public Project getCurrentProject() {
        return currentProject;
    }

    public void setCurrentProject(Project currentProject) {
        this.currentProject = currentProject;
    }

    public int getLastPerformedAction() {
        return lastPerformedAction;
    }

    public void setLastPerformedAction(int lastPerformedAction) {
        this.lastPerformedAction = lastPerformedAction;
    }

    @Override
    public int hashCode() {
        return getRdfId() == null ? 0 : getRdfId().value().hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        return true;
    }


    public int compareTo(Object o) {
        return (this.equals(o) || this.getRdfId() == null || ((RdfModel) o).getRdfId() == null
                ? 0
                : this.getRdfId().value().toString().compareTo(((RdfModel) o).getRdfId().value().toString()));
    }


}
