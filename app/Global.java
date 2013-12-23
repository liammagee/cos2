import init.BasicInit;
import org.apache.commons.io.FileUtils;
import play.*;
import play.libs.*;

import java.io.IOException;
import java.util.*;

import com.avaje.ebean.*;

import models.*;

public class Global extends GlobalSettings {

    public void onStart(Application app) {
        InitialData.insert(app);
    }

    static class InitialData {

        public static void insert(Application app) {
            if(Ebean.find(User.class).findRowCount() == 0) {
                // Reading the evolution file
                /*
                String evolutionContent = null;
                try {
                    evolutionContent = FileUtils.readFileToString(
                            app.getWrappedApplication().getFile("conf/evolutions/default/1.sql"));
                } catch (IOException e) {
                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                }

                // Splitting the String to get Create & Drop DDL
                String[] splittedEvolutionContent = evolutionContent.split("# --- !Ups");
                String[] upsDowns = splittedEvolutionContent[1].split("# --- !Downs");
                String createDdl = upsDowns[0];
                String dropDdl = upsDowns[1];

                Ebean.execute(Ebean.createCallableSql(dropDdl));
                Ebean.execute(Ebean.createCallableSql(createDdl));
                */

                BasicInit.doInit();
            }
            BasicInit.assignGRIIndicatorsToSubdomains();
        }

    }

}