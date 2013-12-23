package init;



import com.clarkparsia.empire.Empire;
import com.clarkparsia.empire.impl.RdfQuery;
import com.clarkparsia.empire.jena.JenaEmpireModule;
import models.*;
import init.transform.*;
import org.xml.sax.SAXException;

import javax.persistence.EntityManager;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: E65691
 * Date: 12/05/11
 * Time: 5:11 PM
 * To change this template use File | Settings | File Templates.
 */
public class BasicInit {

    static Domain economy = null;
    static Domain ecology = null;
    static Domain politics = null;
    static Domain culture = null;
    static IndicatorSet indSetGRI = null;
    static IndicatorSet indSetOther = null;
    public static User defaultUser = null;

    public static void doInit() {

        // Add hooks for supplied args

        // Remove EVERYTHING from the triple store
        RDFDataStorage storage = new RDFDataStorage();
        storage.clearTriples();


        // Set up the empire configuration
        Empire.init(new JenaEmpireModule());
        EntityManager aManager = Persistence.createEntityManagerFactory("SDB2").createEntityManager();

        addDomainsAndSubDomains(aManager);
        addUsers(aManager);
        addIndicatorSets(aManager);
        addIndicators(aManager);
        addWaterCrisisProject(aManager, defaultUser);
        addTehranAirPollutionProject(aManager, defaultUser);
    }

    public static EntityManager getEntityManager() {
        Empire.init(new JenaEmpireModule());
        EntityManager aManager = Persistence.createEntityManagerFactory("SDB2").createEntityManager();
        return aManager;
    }

    public static void addWaterCrisisProject(EntityManager aManager, User creator) {
        Project project = new Project();
        project.setProjectProgress(new ProjectProgress());
        project.setProjectName("Water Crisis");
        project.setCreator(creator);
        project.setVisibility(new Integer(1));
        project.setCreatedAt(new Date());
        project.setProjectDescription("Water crisis is a term used to refer to the world�s water resources relative to human demand. The term has been applied to the worldwide water situation by the United Nations and other world organizations.");
        project.setGeneralIssue("Overall scarcity of usable water and water pollution");
        project.setNormativeGoal("Have enough water for everybody and reduce water pollution");

        // Set up users and Permissions
        project.setCreator(defaultUser);
        Query query = aManager.createQuery("WHERE {?result a <http://circlesofsustainability.org/ontology#User>}");

        // this query should return instances of type Project
        query.setHint(RdfQuery.HINT_ENTITY_CLASS, User.class);

        List aResults = query.getResultList();
        for (Iterator iterator = aResults.iterator(); iterator.hasNext(); ) {
            User user = (User) iterator.next();
            if (!user.equals(defaultUser))
                project.addCollaborator(user);
        }


        ArrayList<CriticalIssue> ciList = new ArrayList<CriticalIssue>();


        /* Critical Issue: Inadequate access to drinking water */
        CriticalIssue accessWaterDrinkingCI = new CriticalIssue();
        accessWaterDrinkingCI.setCreator(defaultUser);
        accessWaterDrinkingCI.setName("Inadequate access to drinking water");
        accessWaterDrinkingCI.setDescription("Inadequate access to safe drinking water for about 884 million people");
        accessWaterDrinkingCI.setDomain(economy);
        accessWaterDrinkingCI.addSubdomain(economy.getSubdomain("Exchange and Transfer"));
        ciList.add(accessWaterDrinkingCI);


        /* Critical Issue: Inadequate access to water for sanitation */
        CriticalIssue accessWaterSanitationCI = new CriticalIssue();
        accessWaterSanitationCI.setCreator(defaultUser);
        accessWaterSanitationCI.setName("Inadequate access to water for sanitation");
        accessWaterSanitationCI.setDescription("Inadequate access to water for sanitation and waste disposal for 2.5 billion people");
        accessWaterSanitationCI.setDomain(economy);
        accessWaterSanitationCI.addSubdomain(economy.getSubdomain("Exchange and Transfer"));
        ciList.add(accessWaterSanitationCI);


        /* Critical Issue: Groundwater overdrafting */
        CriticalIssue groundwaterOverdraftingCI = new CriticalIssue();
        groundwaterOverdraftingCI.setCreator(defaultUser);
        groundwaterOverdraftingCI.setName("Groundwater overdrafting");
        groundwaterOverdraftingCI.setDescription("Groundwater excessive use leading to diminished agricultural yields");
        groundwaterOverdraftingCI.setDomain(economy);
        groundwaterOverdraftingCI.addSubdomain(economy.getSubdomain("Consumption and Use"));
        ciList.add(groundwaterOverdraftingCI);


        /* Critical Issue: Water pollution */
        CriticalIssue waterPollutionCI = new CriticalIssue();
        waterPollutionCI.setCreator(defaultUser);
        waterPollutionCI.setName("Overuse and pollution of Water");
        waterPollutionCI.setDescription("Overuse and pollution of water resources harming biodiversity");
        waterPollutionCI.setDomain(ecology);
        waterPollutionCI.addSubdomain(ecology.getSubdomain("Emission and Waste"));

        ArrayList<IssueComponent> compList = new ArrayList<IssueComponent>();
        // Component
        IssueComponent waterOveruseComp = new IssueComponent();
        waterOveruseComp.setName("Water Overuse");
        waterOveruseComp.setDescription("Overuse of water ressources");
        waterOveruseComp.setDomain(economy);
        waterOveruseComp.addSubdomain(economy.getSubdomain("Consumption and Use"));
        waterOveruseComp.setParentIssue(waterPollutionCI);
        compList.add(waterOveruseComp);

        // Component
        IssueComponent waterPollutionComp = new IssueComponent();
        waterPollutionComp.setName("Water Pollution");
        waterPollutionComp.setDescription("Pollution of water ressources");
        waterPollutionComp.setDomain(ecology);
        waterPollutionComp.addSubdomain(ecology.getSubdomain("Emission and Waste"));
        waterPollutionComp.setParentIssue(waterPollutionCI);
        compList.add(waterPollutionComp);

        // Component
        IssueComponent harmingBiodiversityComp = new IssueComponent();
        harmingBiodiversityComp.setName("Harming biodiversity");
        harmingBiodiversityComp.setDescription("Biodiversity is affected by water use and pollution");
        harmingBiodiversityComp.setDomain(ecology);
        harmingBiodiversityComp.addSubdomain(ecology.getSubdomain("Flora and Fauna"));
        harmingBiodiversityComp.setParentIssue(waterPollutionCI);
        compList.add(harmingBiodiversityComp);

        waterPollutionCI.setComponentIssues(compList);
        ciList.add(waterPollutionCI);


        /* Critical Issue: Conflict for water */
        CriticalIssue conflictForWaterCI = new CriticalIssue();
        conflictForWaterCI.setCreator(defaultUser);
        conflictForWaterCI.setName("Conflict for water");
        conflictForWaterCI.setDescription("Regional conflicts over scarce water resources sometimes resulting in warfare");
        conflictForWaterCI.setDomain(politics);
        conflictForWaterCI.addSubdomain(politics.getSubdomain("Security and Conflict"));
        ciList.add(conflictForWaterCI);

        project.setCriticalIssues(ciList);

        project.save();
    }

    public static void addTehranAirPollutionProject(EntityManager aManager, User creator) {
        retrieveDomains();

        Project project = new Project();
        project.setProjectProgress(new ProjectProgress());
        project.setProjectName("Tehran Air Pollution");
        project.setCreator(creator);
        project.setCreatedAt(new Date());
        project.setProjectDescription("Tehran, the capital city of Iran, is the largest urban area of Iran with a population of 8,700,000 in 2011. The city also is ranked as one of the largest cities in Western Asia, and is the 19th largest globally. As in other large cities, Tehran is faced with serious air quality problems. Overall, 20% of the total energy of the country is consumed in Tehran. Pollutants such as PM10, SO2, NO2, HC, O3 and CO are the major air pollutants in Tehran, about 80-85% of which is produced by mobile sources of pollution.");
        project.setGeneralIssue("Poor urban air quality, due to fossil fuel emissions.");
        project.setNormativeGoal("Reduce fossil fuel emissions.");

        ArrayList<CriticalIssue> ciList = new ArrayList<CriticalIssue>();



        CriticalIssue geographicLocation = new CriticalIssue();
        geographicLocation.setCreator(creator);
        geographicLocation.setName("Geographical location of the city");
        geographicLocation.setDescription("With the location of 35° 41' N - 51° 25' E, and an altitude of 1000–1800 meters above mean sea level, Tehran is located in valleys and is surrounded on the north, northwest, east and southeast by high to medium high (3800–1000 m) mountain ranges. The mountain range stops the flow of the humid wind to the main capital and prevents the polluted air from being carried away from the city. Thus, during winter, the lack of wind and cold air causes the polluted air to be trapped within the city.");
        geographicLocation.setDomain(ecology);
        geographicLocation.addSubdomain(Subdomain.find.where().eq("name", "Habitat and Land").findUnique());
        ciList.add(geographicLocation);


        CriticalIssue useOfPrivateCars = new CriticalIssue();
        useOfPrivateCars.setCreator(creator);
        useOfPrivateCars.setName("The use of private cars");
        useOfPrivateCars.setDescription("Citizens are not encouraged to use public transport. The city has a capacity for 700,000 registered cars, yet 3 million roam its streets on a daily basis. According to one study, cars account for 70% to 80% of the city’s air pollution.");
        useOfPrivateCars.setDomain(culture);
        useOfPrivateCars.addSubdomain(Subdomain.find.where().eq("name", "Enquiry and Learning").findUnique());
        ciList.add(useOfPrivateCars);



        CriticalIssue poorPublicTransportService = new CriticalIssue();
        poorPublicTransportService.setCreator(creator);
        poorPublicTransportService.setName("Poor public transport service");
        poorPublicTransportService.setDescription("Tehran Metro – the city underground railway, carries more than 2 million passengers per day. Other types of public transport are not fully developed like bus lines. Furthermore, the taxi rate is very expensive. However, this type of transport is still the most used type, which also has the direct effect on the air pollution problem.");
        poorPublicTransportService.setDomain(ecology);
        poorPublicTransportService.addSubdomain(Subdomain.find.where().eq("name", "Infrastructure and Constructions").findUnique());
        ciList.add(poorPublicTransportService);


        CriticalIssue lowQualityPetrol = new CriticalIssue();
        lowQualityPetrol.setCreator(creator);
        lowQualityPetrol.setName("Low quality of the petrol made in Iran");
        lowQualityPetrol.setDescription("As sanctions on imports of refined gasoline have forced the country to turn to low-quality fuel.  All vehicles in Tehran is used the low quality petrol. The government has tried to replace the fossil fuel consumption with the gas option by encouraging drivers to facilitate their car with the second alternative. However, the conversion process from fossil fuel to gas is costly. Most drivers do not find this option economic.");
        lowQualityPetrol.setDomain(politics);
        lowQualityPetrol.addSubdomain(Subdomain.find.where().eq("name", "Security and Conflict").findUnique());
        lowQualityPetrol.addSubdomain(Subdomain.find.where().eq("name", "Consumption and Use").findUnique());
        ciList.add(lowQualityPetrol);


        CriticalIssue industrialisingCity = new CriticalIssue();
        industrialisingCity.setCreator(creator);
        industrialisingCity.setName("Moving towards an industrialising city");
        industrialisingCity.setDescription("Tehran, as Iran’s capital and one of the world’s megacities, is moving towards industrialisation to strengthen its economy. The actions to support this aspect of the city have directly and indirectly polluted Tehran.  On the other hand, the severity of the air pollution in certain time of the year has forced the government to shut down the city for a few days by asking people to stay at their homes. For example, in November 2013, the kindergartens, elementary schools and universities were closed for three days. This action has considerable drawbacks of the economy of Tehran and the country as a whole. Consequently air pollution can be see as both a result of economic activity, and a threat to further economic growth.");
        industrialisingCity.setDomain(economy);
        industrialisingCity.addSubdomain(Subdomain.find.where().eq("name", "Consumption and Use").findUnique());
        ciList.add(industrialisingCity);

        project.setCriticalIssues(ciList);

        project.save();
    }

    private static void addUsers(EntityManager aManager) {
        // Add some users
        User lida = new User("lida", "test");
        User liam = new User("liam", "test");

        lida.save();
        liam.save();

        defaultUser = liam;
    }


    private static void retrieveDomains() {
        List<Domain> domains = Domain.all();
        economy = domains.get(0);
        ecology = domains.get(1);
        politics = domains.get(2);
        culture = domains.get(3);
    }

    private static void addDomainsAndSubDomains(EntityManager aManager) {
        populateDomains(aManager);

        ecology.save();
        economy.save();
        politics.save();
        culture.save();
    }


    private static void populateDomains(EntityManager aManager) {
        // Add current domains and subdomains
        economy = new Domain("Economy");
        economy.setDescription("The economic domain is defined in terms of activities associated with the production, use, movement, and management of resources, where the concept of ‘resources’ is used in the broadest sense of that word.");
        economy.addSubdomain(new Subdomain(1, "Resourcing and Production"));
        economy.addSubdomain(new Subdomain(2, "Exchange and Transfer"));
        economy.addSubdomain(new Subdomain(3, "Regulation and Counting"));
        economy.addSubdomain(new Subdomain(4, "Consumption and Use"));
        economy.addSubdomain(new Subdomain(5, "Labour and Welfare"));
        economy.addSubdomain(new Subdomain(6, "Technology and Fabrication"));
        economy.addSubdomain(new Subdomain(7, "Wealth and Allocation"));

        ecology = new Domain("Ecology");
        ecology.setDescription("The ecological domain is defined in terms of the intersection between the social and the natural realms, focussing on the important dimension of human engagement with and within nature, but also including the build environment.");
        ecology.addSubdomain(new Subdomain(8, "Water and Air"));
        ecology.addSubdomain(new Subdomain(9, "Flora and Fauna"));
        ecology.addSubdomain(new Subdomain(10, "Habitat and Land"));
        ecology.addSubdomain(new Subdomain(11, "Place and Abode"));
        ecology.addSubdomain(new Subdomain(12, "Materials and Energy"));
        ecology.addSubdomain(new Subdomain(13, "Infrastructure and Constructions"));
        ecology.addSubdomain(new Subdomain(14, "Emission and Waste"));

        politics = new Domain("Politics");
        politics.setDescription("The political domain is defined in terms of practices of authorization, legitimation and regulation.");
        politics.addSubdomain(new Subdomain(15, "Organization and Governance"));
        politics.addSubdomain(new Subdomain(16, "Law and Justice"));
        politics.addSubdomain(new Subdomain(17, "Communication and Dissemination"));
        politics.addSubdomain(new Subdomain(18, "Representation and Negotiation"));
        politics.addSubdomain(new Subdomain(19, "Security and Conflict"));
        politics.addSubdomain(new Subdomain(20, "Dialogue and Reconciliation"));
        politics.addSubdomain(new Subdomain(21, "Ethics and Duty"));

        culture = new Domain("Culture");
        culture.setDescription("The cultural domain is defined in terms of practices, discourses, and material expressions, which, over time, express continuities and discontinuities of meaning.");
        culture.addSubdomain(new Subdomain(22, "Engagement and Affiliation"));
        culture.addSubdomain(new Subdomain(23, "Symbolism and Creativity"));
        culture.addSubdomain(new Subdomain(24, "Memory and Projection"));
        culture.addSubdomain(new Subdomain(25, "Belief and Faith"));
        culture.addSubdomain(new Subdomain(26, "Enquiry and Learning"));
        culture.addSubdomain(new Subdomain(27, "Health and Recreation"));
        culture.addSubdomain(new Subdomain(28, "Gender and Reproduction"));
    }

    private static void addIndicatorSets(EntityManager aManager) {
        // INDICATOR SETS

        indSetOther = new IndicatorSet();
        indSetOther.setCreator(defaultUser);
        indSetOther.setSource("Other");
        indSetOther.setName("Other Indicator Set");
        indSetOther.setDescription("Another set of indicators");

        indSetOther.save();

        // Load GRI using SAX Parser
        SAXGRIDriver saxgriDriver = null;
        saxgriDriver.baseDir = "conf/";
        saxgriDriver = new SAXGRIDriver(aManager);
        try {
            saxgriDriver.parser();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (SAXException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

    }


    private static void addIndicators(EntityManager aManager) {
        Indicator waterConsumption = new Indicator();
        waterConsumption.setCreator(defaultUser);
        waterConsumption.setIdentifyingCode("waterconsumption");
        waterConsumption.setName("Water Consumption");
        waterConsumption.setDescription("The total water a consumption this year (in m3)");
        waterConsumption.setDomain(economy);
        waterConsumption.setSubdomain(economy.getSubdomain("Consumption and Use"));
        waterConsumption.setIndicatorSet(indSetGRI);

        /* CREATION OF AN INDICATOR: RAIN AMOUNT */
        Indicator rainAmountInd = new Indicator();
        rainAmountInd.setCreator(defaultUser);
        rainAmountInd.setIdentifyingCode("rainamount");
        rainAmountInd.setName("Rain Amount");
        rainAmountInd.setDescription("Average amount of rain (in mm) this year.");
        rainAmountInd.setDomain(ecology);
        rainAmountInd.setSubdomain(ecology.getSubdomain("Water and Air"));
        rainAmountInd.setIndicatorSet(indSetGRI);
        rainAmountInd.setSource("GRI");

        /* CREATION OF AN INDICATOR: BUSHFIRES */
        Indicator bushfireInd = new Indicator();
        bushfireInd.setCreator(defaultUser);
        bushfireInd.setIdentifyingCode("bushfires");
        bushfireInd.setName("Bushfires");
        bushfireInd.setDescription("Number of important bushfires (more than 1000ha burnt) this year.");
        bushfireInd.setDomain(ecology);
        bushfireInd.setSubdomain(ecology.getSubdomain("Materials and Energy"));
        bushfireInd.setIndicatorSet(indSetGRI);
        bushfireInd.setSource("GRI");
        Target target = new Target();
        target.setAssociatedIndicator(bushfireInd);
        target.setValue("1");

        /* CREATION OF THE INDICATOR: WATER SUPPLY */
        Indicator waterSupplyInd = new Indicator();
        waterSupplyInd.setCreator(defaultUser);
        waterSupplyInd.setIdentifyingCode("watersupply");
        waterSupplyInd.setName("Water Supply");
        waterSupplyInd.setDescription("The total water supply of the territory (in m3)");
        waterSupplyInd.setDomain(ecology);
        waterSupplyInd.setSubdomain(ecology.getSubdomain("Water and Air"));
        waterSupplyInd.setIndicatorSet(indSetGRI);
        waterSupplyInd.setSource("GRI");

        /* CREATION OF THE INDICATOR: WATER CONSUMPTION / PERSON */
        Indicator waterConsumptionPerPersonInd = new Indicator();
        waterConsumptionPerPersonInd.setCreator(defaultUser);
        waterConsumptionPerPersonInd.setIdentifyingCode("waterConsumptionPerPerson");
        waterConsumptionPerPersonInd.setName("Water Consumption / Person");
        waterConsumptionPerPersonInd.setDescription("The water consumption this year (in m3)");
        waterConsumptionPerPersonInd.setDomain(economy);
        waterConsumptionPerPersonInd.setSubdomain(economy.getSubdomain("Consumption and Use"));
        waterConsumptionPerPersonInd.setIndicatorSet(indSetOther);
        waterConsumptionPerPersonInd.setSource("City of Melbourne");

        /* CREATION OF THE INDICATOR: NUMBER OF DAMS */
        Indicator numberDamsInd = new Indicator();
        numberDamsInd.setCreator(defaultUser);
        numberDamsInd.setIdentifyingCode("numberDams");
        numberDamsInd.setName("Number of Dams");
        numberDamsInd.setDescription("The number of dams in the region");
        numberDamsInd.setDomain(ecology);
        numberDamsInd.setSubdomain(ecology.getSubdomain("Infrastructure and Constructions"));
        numberDamsInd.setIndicatorSet(indSetGRI);
        numberDamsInd.setSource("GRI");

        /* CREATION OF THE INDICATOR: NUMBER OF CRIMES */
        Indicator numberCrimesInd = new Indicator();
        numberCrimesInd.setCreator(defaultUser);
        numberCrimesInd.setIdentifyingCode("numberCrimes");
        numberCrimesInd.setName("Number of Crimes");
        numberCrimesInd.setDescription("The number of crimes recorded by police last year");
        numberCrimesInd.setDomain(politics);
        numberCrimesInd.setSubdomain(politics.getSubdomain("Security and Conflict"));
        numberCrimesInd.setIndicatorSet(indSetOther);
        numberCrimesInd.setSource("City of Melbourne");

        waterConsumption.save();
        rainAmountInd.save();
        bushfireInd.save();
        waterSupplyInd.save();
        waterConsumptionPerPersonInd.save();
        numberDamsInd.save();
        numberCrimesInd.save();

        List<Indicator> indicators = indSetOther.getIndicators();
        indicators.add(waterConsumption);
        indicators.add(rainAmountInd);
        indicators.add(bushfireInd);
        indicators.add(waterSupplyInd);
        indicators.add(waterConsumptionPerPersonInd);
        indicators.add(numberDamsInd);
        indicators.add(numberCrimesInd);
        indSetOther.setIndicators(indicators);
        indSetOther.save();
    }

    public static void assignGRIIndicatorsToSubdomains() {
        int[] orderedSubdomainIds = {
            8, 10, 12, 14, 12, 13, 12, 6, 11, 5, 7, 5, 5, 5, 15, 5, 1, 1, 1, 3, 4, 3, 2, 2, 7, 7, 15, 1, 1, 7, 7, 7, 7, 3, 15, 11, 16, 9, 8, 21, 17, 16, 22, 18, 16, 15, 16, 22, 15, 12, 12, 2, 17, 27, 27, 16, 27, 26, 26, 26, 22, 28, 27, 27, 17, 16, 17, 17, 16, 16, 10, 17, 16, 26, 15, 17, 18, 16, 10
        };
        for (int i = 0; i < orderedSubdomainIds.length; i++) {
            Indicator gri = Indicator.find.byId(new Long(i + 1));
            Subdomain chosenSubdomain = Subdomain.find.byId(new Long(orderedSubdomainIds[i]));
            List<Subdomain> subdomains = new ArrayList<Subdomain>(); 
            subdomains.add(chosenSubdomain);
            gri.setSubdomains(subdomains);
            gri.save();

        }
    }
}
