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
//        ciList.add(poorPublicTransportService);


        CriticalIssue lowQualityPetrol = new CriticalIssue();
        lowQualityPetrol.setCreator(creator);
        lowQualityPetrol.setName("Low quality of the petrol made in Iran");
        lowQualityPetrol.setDescription("As sanctions on imports of refined gasoline have forced the country to turn to low-quality fuel.  All vehicles in Tehran is used the low quality petrol. The government has tried to replace the fossil fuel consumption with the gas option by encouraging drivers to facilitate their car with the second alternative. However, the conversion process from fossil fuel to gas is costly. Most drivers do not find this option economic.");
        lowQualityPetrol.setDomain(politics);
        lowQualityPetrol.addSubdomain(Subdomain.find.where().eq("name", "Security and Conflict").findUnique());
        lowQualityPetrol.addSubdomain(Subdomain.find.where().eq("name", "Consumption and Use").findUnique());
//        ciList.add(lowQualityPetrol);


        CriticalIssue industrialisingCity = new CriticalIssue();
        industrialisingCity.setCreator(creator);
        industrialisingCity.setName("Moving towards an industrialising city");
        industrialisingCity.setDescription("Tehran, as Iran’s capital and one of the world’s megacities, is moving towards industrialisation to strengthen its economy. The actions to support this aspect of the city have directly and indirectly polluted Tehran.  On the other hand, the severity of the air pollution in certain time of the year has forced the government to shut down the city for a few days by asking people to stay at their homes. For example, in November 2013, the kindergartens, elementary schools and universities were closed for three days. This action has considerable drawbacks of the economy of Tehran and the country as a whole. Consequently air pollution can be see as both a result of economic activity, and a threat to further economic growth.");
        industrialisingCity.setDomain(economy);
        industrialisingCity.addSubdomain(Subdomain.find.where().eq("name", "Consumption and Use").findUnique());
//        ciList.add(industrialisingCity);

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

        buildUNHabitatIndicators();
        buildOecdIndicators();

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


    private static void buildUNHabitatIndicators() {

        IndicatorSet unhabitatIndicatorSet = new IndicatorSet();
        unhabitatIndicatorSet.setCreator(defaultUser);
        unhabitatIndicatorSet.setSource("UN Habitat");
        unhabitatIndicatorSet.setName("UN Habitat Agenda Indicators");
        unhabitatIndicatorSet.setDescription("");

        String[][] unhabitatIndicators = {
            {"Shelter", "Promote the right to adequate housing", "Key indicator 1: durable structures", "6"}, 
            {"Shelter", "Promote the right to adequate housing", "Key indicator 2: overcrowding", "3"}, 
            {"Shelter", "Promote the right to adequate housing", "check-list 1: right to adequate housing", "4"}, 
            {"Shelter", "Promote the right to adequate housing", "extensive indicator 1: housing price and rent-to-income", "14"}, 
            {"Shelter", "Provide security of tenure", "Key indicator 3: secure tenure", "19"}, 
            {"Shelter", "Provide security of tenure", "extensive indicator 2: authorized housing", "16"}, 
            {"Shelter", "Provide security of tenure", "extensive indicator 3: evictions", "19"}, 
            {"Shelter", "Provide equal access to credit ", "check-list 2: housing finance", "9"}, 
            {"Shelter", "Provide equal access to land ", "extensive indicator 4: land price-to-income", "8"}, 
            {"Shelter", "Promote access to basic services", "Key indicator 4: access to safe water", "1"}, 
            {"Shelter", "Promote access to basic services", "Key indicator 5: access to improved sanitation", "7"}, 
            {"Shelter", "Promote access to basic services", "Key indicator 6: connection to services", "5"}, 
            {"Social development and eradication of poverty", "Provide equal opportunities for a safe and healthy life", "Key indicator 7: under-five mortality", "27"}, 
            {"Social development and eradication of poverty", "Provide equal opportunities for a safe and healthy life", "Key indicator 8: homicides", "19"}, 
            {"Social development and eradication of poverty", "Provide equal opportunities for a safe and healthy life", "check-list 3: urban violence", "19"}, 
            {"Social development and eradication of poverty", "Provide equal opportunities for a safe and healthy life", "extensive indicator 5: HIV prevalence", "27"}, 
            {"Social development and eradication of poverty", "Promote social integration and support disadvantaged groups", "Key indicator 9: poor households", "14"}, 
            {"Social development and eradication of poverty", "Promote gender equality in human settlements development", "Key indicator 10: literacy rates", "26"}, 
            {"Social development and eradication of poverty", "Promote gender equality in human settlements development", "check-list 4: gender inclusion", "28"}, 
            {"Social development and eradication of poverty", "Promote gender equality in human settlements development", "extensive indicator 6: school enrolment", "26"}, 
            {"Social development and eradication of poverty", "Promote gender equality in human settlements development", "extensive indicator 7: women councilors", "28"}, 
            {"Environmental Management", "Promote geographically balanced settlement structures", "Key indicator 11: urban population growth", "28"}, 
            {"Environmental Management", "Promote geographically balanced settlement structures", "Key indicator 12: planned settlements", "15"}, 
            {"Environmental Management", "Manage supply and demand for water in an effective manner", "Key indicator 13: price of water ", "8"}, 
            {"Environmental Management", "Manage supply and demand for water in an effective manner", "extensive indicator 8: water consumption", "11"}, 
            {"Environmental Management", "Reduce urban pollution Key indicator 14: wastewater treated", "Key indicator 15: solid waste disposal", "7"}, 
            {"Environmental Management", "Reduce urban pollution Key indicator 14: wastewater treated", "extensive indicator 9: regular solid waste collection", "7"}, 
            {"Environmental Management", "Prevent disasters and rebuild settlements", "check-list 5: disaster prevention and mitigation instruments", "15"}, 
            {"Environmental Management", "Prevent disasters and rebuild settlements", "extensive indicator 10: houses in hazardous locations", "6"}, 
            {"Environmental Management", "Promote effective and environmentally sound transportation systems", "Key indicator 16: travel time", "6"}, 
            {"Environmental Management", "Promote effective and environmentally sound transportation systems", "extensive indicators 11: transport modes", "6"}, 
            {"Environmental Management", "Support mechanisms to prepare and implement local environmental plans and local Agenda 21 initiatives", "check-list 6: local environmental plans", "15"}, 
            {"Economic Development", "Strengthen small and microenterprises, particularly those developed by women", "Key indicator 17: informal employment", "8"}, 
            {"Economic Development", "Encourage public-private sector partnership and stimulate productive employment opportunities", "Key indicator 18: city product", "14"}, 
            {"Economic Development", "Encourage public-private sector partnership and stimulate productive employment opportunities", "Key indicator 19: unemployment", "12"}, 
            {"Governance", "Promote decentralisation and strengthen local authorities", "Key indicator 20: local government revenue", "9"}, 
            {"Governance", "Promote decentralisation and strengthen local authorities", "Check-list 7: decentralization", "18"}, 
            {"Governance", "Encourage and support participation and civic engagement", "Check-list 8: citizens participation", "18"}, 
            {"Governance", "Encourage and support participation and civic engagement", "extensive indicator 12: voters participation", "18"}, 
            {"Governance", "Encourage and support participation and civic engagement", "extensive indicator 13: civic associations", "20"}, 
            {"Governance", "Ensure transparent, accountable and efficient governance of towns, cities and metropolitan areas", "Check-list 9: transparency and accountability", "15"}  
        };

        for (int i = 0; i < unhabitatIndicators.length; i++) {
            String[] indicatorStrings = unhabitatIndicators[i];
            Indicator unhabitatIndicator = new Indicator();
            unhabitatIndicator.setCreator(defaultUser);
            unhabitatIndicator.setCategory(indicatorStrings[0]);
            unhabitatIndicator.setSubcategory(indicatorStrings[1]);
            unhabitatIndicator.setName(indicatorStrings[2]);
            Long subdomainId = new Long(indicatorStrings[3]);
            List<Subdomain> subdomains = new ArrayList<Subdomain>();
            Subdomain subdomain = Subdomain.find.byId(subdomainId);
            subdomains.add(subdomain);
            unhabitatIndicator.setSubdomains(subdomains);
            unhabitatIndicator.setIndicatorSet(unhabitatIndicatorSet);
            unhabitatIndicator.setSource("UN Habitat");
            unhabitatIndicator.save();
        }
        unhabitatIndicatorSet.save();
    }

    private static void buildOecdIndicators() {
        IndicatorSet oecdIndicatorSet = new IndicatorSet();
        oecdIndicatorSet.setCreator(defaultUser);
        oecdIndicatorSet.setSource("OECD");
        oecdIndicatorSet.setName("OECD Green Growth");
        oecdIndicatorSet.setDescription("");

        String[][] oecdIndicators = {
            {"The socio-economic context and characteristics of growth", "Economic growth, productivity and competitiveness", "Economic growth and structure ", "GDP growth and structure", "Gross domestic product to measure market and government production and the associated economic activity. This indicator relates to the sphere of production. As a ‘gross’ measure, no account is taken of the depreciation of produced asset nor of the depletion of natural assets. However, GDP is the most widely-used measure of economic growth and remains a central variablefor macro-economic management and economic activity. ", "8"},
            {"The socio-economic context and characteristics of growth ", "Economic growth, productivity and competitiveness", "Economic growth and structure ", "Net national income", "Net national income to capture the average material well-being of individuals and households. These income flows can differ from GDP becausethey take into account the depreciation of produced capital, and income flows between residents and the rest of the world. Real income is also influenced by changes in the terms-of-trade, the development of export prices relative to import prices. Rising terms of trade permit more imports to be purchased for given value of exports, thereby increasing the purchasing power of nominal Income. ", "9"},
            {"The socio-economic context and characteristics of growth ", "Economic growth, productivity and competitiveness", "Productivity and trade", "Labour productivity", "Long-term competitiveness, a driver of sustained material living standards and captured by a measure of countries’ relative unit labour costs. Unit labour costs reflect the combined effects of wage developments and labourProductivity. An important source of labour productivity is multi-factor productivity growth – the increase in economic output that cannot be explained by increases in economic inputs – that raises the rate of output growth and therefore domestic income. Multi-factor productivity is often associated with technological change and innovation. ", "12"},
            {"The socio-economic context and characteristics of growth ", "Economic growth, productivity and competitiveness", "Productivity and trade", "Multi-factor productivity", "Multi-factor productivity is often associatedwith technological change and innovation. One notes that the measure presented here only recognises labour and capital inputs and not primary inputs of natural capital that also feed into production. Some of the contribution of such natural capital to output growth is thus wrapped in with the productivity measure.", "12"}, 
            {"The socio-economic context and characteristics of growth ", "Economic growth, productivity and competitiveness", "Productivity and trade", "Trade weighted unit labour costs", "A proxy measure of international price competitiveness in the form of trade-weighted unit labour costs. Changes in unit labour costs approximateoutput price developments as labour accounts for an important share of finaloutput.", "12"}, 
            {"The socio-economic context and characteristics of growth ", "Economic growth, productivity and competitiveness", "Productivity and trade", "Relative importance of trade: (exports + imports)/GDP", "The relative importance of international trade in countries’ economies. Thismeasure indicates exposure to international competition abroad anddomestically.", "9"}, 
            {"The socio-economic context and characteristics of growth ", "Economic growth, productivity and competitiveness", "Productivity and trade", "Inflation and commodity prices", "Inflation and commodity prices: commodity prices are directly related to important natural resources such as minerals or fossil fuels. Prices are powerful signals, in particular the longer-term evolution of relative prices can signal scarcity or abundance and affect economic behaviour. Overly volatile price movements on the other hand tend to send unreliable signals that may or maynot be conducive to more environmentally-friendly growth.", "11"}, 
            {"The socio-economic context and characteristics of growth ", "Labour markets,education andIncome", "Labour markets", "Labour force participation", "Labour force participation rates, measuring the part of an economy’s working-age population that is economically active. It provides an indication ofthe relative size of the supply of labour available for the production of goods and services.", "12"}, 
            {"The socio-economic context and characteristics of growth ", "Labour markets,education andIncome", "Labour markets", "Unemployment rates", "Unemployment rates which represent the share of people unemployed relative to the persons in the labour force. High and persistent unemploymentrates signal an underutilisation of an economy’s single most important resource, labour and human capital. By implication, there is an un-exploitedgrowth potential.", "12"}, 
            {"The socio-economic context and characteristics of growth ", "Labour markets,education andIncome", "Socio-demographic patterns", "Population growth, structure & density", "Population density, the number of inhabitants per square kilometre. While average economy-wide density rates give a first impression of developments, they cannot account for concentration and population density inside a country, in particular the differences between rural and urban areas.", "28"}, 
            {"The socio-economic context and characteristics of growth ", "Labour markets,education andIncome", "Socio-demographic patterns", "Life expectancy: years of healthy life at birth", "Old age dependency ratio, the number of inhabitants aged 20-64 over thenumber of inhabitants aged 65 or more.Years of healthy life expectancy at birth, which refers to the average numberof years that a person can expect to live in \"full health\" by taking into accountyears lived in less than full health due to disease and/or injury. Health is anessential element of well-being and economic development. Health risksassociated with low-quality environmental conditions such as chronic diseases,injuries and infectious diseases reduce people’s well-being and imposeeconomic costs on households, companies and governments.", "27"}, 
            {"The socio-economic context and characteristics of growth ", "Labour markets,education andIncome", "Socio-demographic patterns", "Income inequality: GINI coefficient", "Income inequality, measured by the Gini coefficient bounded between 0 and1 with higher values related to higher income inequality.", "14"}, 
            {"The socio-economic context and characteristics of growth ", "Labour markets,education andIncome", "Socio-demographic patterns", "Educational attainment: Level of and access to education", "Access to education, an indicator of a country’s investment in human capital, is measured by university-level enrolment. The level of education is measured by university-level graduation rate of all students from tertiary-type of programs. Human capital development is a driver of growth in its own right. Education induces behavioural changes and raises skills, including for the adoption and adaption of environmentally-friendly processes, products and technologies.", "26"}, 
            {"Environmental and resource productivity", "Carbon & energyproductivity", "1. CO2 productivity", "1.1. Production-based CO2 productivity", "Production based CO2 productivity - GDP generated per unit of CO2 emitted -and CO2 intensities per capita for the period 1990 to 2008. The emissionspresented here are gross direct emissions, emitted within the national territoryand excluding bunkers, sinks and indirect effects. The CO2 productivity ofproduction informs about the relative decoupling between domestic productionand carbon inputs. It also reflects other environmental issues, in particularemissions of greenhouse gases and air pollution that are correlated with thecarbon intensity of economic production.", "5"}, 
            {"Environmental and resource productivity", "Carbon & energyproductivity", "1. CO2 productivity", "GDP per unit of energy-related CO2 emitted", "", "5"}, 
            {"Environmental and resource productivity", "Carbon & energyproductivity", "1. CO2 productivity", "1.2. Demand-based CO2 productivity", "Demand based CO2 productivity – the real disposable income generated perunit of CO2 emitted -for the period 1995 to 2005. Demand based emissionsreflect the CO2 emitted during all of the various stages of production of thegoods and services consumed in domestic final demand, irrespective of wherethe stages of production occurred. Trends in emissions on this basis serve as adiagnostic complement to the more traditional production based measures.", "5"}, 
            {"Environmental and resource productivity", "Carbon & energyproductivity", "1. CO2 productivity", "Real income per unit of energy-related CO2 emitted", "", "5"}, 
            {"Environmental and resource productivity", "Carbon & energyproductivity", "2. Energy productivity", "2.1. Energy productivity (GDP per unit of TPES)", "Energy productivity and energy intensity by sector (manufacturing, freight transport, passenger transport). Energy productivity, expressed as GDP per unit of total primary energy supply (TPES), and intensities per capita, may reflect, at least partly, efforts to improve energy efficiency and to reduce carbon and other atmospheric emissions. They also reflect structural and climatic factors (see “Interpretation” below). The structure of energy supply is given as a complement. ", "11"}, 
            {"Environmental and resource productivity", "Carbon & energyproductivity", "2. Energy productivity", "2.2. Energy intensity by sector (manufacturing, transport, households, services)", "Share of renewables in TPES and in electricity production. The energy mix, i.e. the structure of energy supply, in terms of primary energy source as a % ofTPES or of total electricity generation is closely related to consumption andproduction patterns and to environmental effects. Renewables are also used inheat generation. Main sources of renewable energy are combustible renewables(mainly wood) and waste, hydro, geothermal, wind and solar energy.", "11"}, 
            {"Environmental and resource productivity", "Carbon & energyproductivity", "2. Energy productivity", "2.3. Share of renewable energyin TPES, in electricity production", "Efficiency is a contributing factor in productivity, but many other elements –often more significant – need also be considered. These include: the structureof the economy (presence of large energy-consuming industries, for instance);the size of the country (higher demand from the transport sector); the climate(higher demand for heating or cooling).", "13"}, 
            {"Environmental and resource productivity", "Resource Productivity", "3. Material Productivity (non-energy)", "3.1 Material productivity (non-energy)Demand based material productivity(comprehensive measure; original units in physical terms) related to realdisposable income", "Material extraction, i.e. domestic extraction “used” (DEU), expressed inabsolute terms, and related changes for individual material groups and foraggregates. The focus is on non-energy materials.Material consumption, i.e. domestic material consumption (DMC), expressed", "5"}, 
            {"Environmental and resource productivity", "Resource Productivity", "3. Material Productivity (non-energy)", "Domestic material productivity (GDP/DMC)                       -Biotic materials (food, other biomass)- Abiotic materials (metallic minerals, industrial minerals)", "Material consumption, i.e. domestic material consumption (DMC), expressed in absolute terms, and related productivity ratios for individual material groups and for aggregates. Productivity is expressed as the amount of economic output generated for a unit of materials consumed. The focus is on non-energy materials.", "5"}, 
            {"Environmental and resource productivity", "Resource Productivity", "3. Material Productivity (non-energy)", "3.2. Waste generation intensities and recovery ratiosBy sector, per unit of GDP or VA, per capita", "Waste: Despite considerable progress, data on waste generation and disposal remains weak in many countries. Further efforts are needed to:Ensure an appropriate monitoring of all waste flows and of related management practices.Improve the international comparability of the data. Fill data gaps with respect to waste prevention measures and other measuresrelated to the 3Rs (reduce, reuse, recycle).", "7"}, 
            {"Environmental and resource productivity", "Resource Productivity", "3. Material Productivity (non-energy)", "3.3. Nutrient flows and balances (N,P) ", "Nitrogen and phosphorus surplus intensities, expressed as the N and P balanceper ha of agricultural land.", "5"}, 
            {"Environmental and resource productivity", "Resource Productivity", "3. Material Productivity (non-energy)", "Nutrient balances in agriculture (N, P) per agricultural land area and change in agricultural output ", "Agricultural nutrient intensity related to changes in agricultural output, expressed as changes in the nitrogen (N) and phosphorus (P) balance per ha ofagricultural land versus changes in agricultural production.", "5"}, 
            {"Environmental and resource productivity", "Resource Productivity", "4. Water productivity ", "VA per unit of water consumed, by sector (for agriculture: irrigation water per hectare irrigated) ", "", "1"}, 
            {"Environmental and resource productivity", "Multi-factorproductivity", " 5. Multi-factor productivity reflecting environmental services", " (comprehensive measure; original units in monetary terms) ", "", "8"}, 
            {"Natural asset base", "Renewable stocks", "6. Freshwater resources", "Available renewable resources (groundwater, surface water, national, territorial) ", "Available freshwater resources expressed as the long term annual average availability in m3 per capita.", "1"}, 
            {"Natural asset base", "Renewable stocks", "6. Freshwater resources", "related abstraction rates", "Abstraction rates and water stress: the intensity of use of freshwater resources, expressed as gross abstractions as a % of total available renewable freshwater resources (including inflows from neighbouring countries) and as a % of internal resources (i.e. precipitations–evapotranspiration).", "1"}, 
            {"Natural asset base", "Renewable stocks", "7. Forest resources", " Forest resourcesArea and volume of forests", "The volume of forest resource stocks, expressed in m3 , and related changessince 1990.", "2"}, 
            {"Natural asset base", "Renewable stocks", "7. Forest resources", " stock changes over time", "The area of forest and wooded land, as a percentage of total land area and inkm2 per capita, and related changes since 1990.", "2"}, 
            {"Natural asset base", "Renewable stocks", "8.  Fish resources", " Fish resourcesProportion of fish stocks within safe biological limits (global)", "The proportion of fish stocks within safe biological limits (global), expressed as the percentage of fish stocks exploited within their level of maximum biological productivity, i.e. stocks that are underexploited, moderately exploited, and fullyexploited. Safe biological limits are the precautionary thresholds advocated by the International Council for the Exploration of the Sea. This indicator is also included in the Millennium Development Goal monitoring framework.", "2"}, 
            {"Natural asset base", "Non-renewablestocks", "9.  Mineral resources", " Mineral resourcesAvailable (global) stocks or reserves of selected minerals (tbd): metallic minerals,industrial minerals, fossil fuels, critical raw materials; and related extraction rates", "", "3"}, 
            {"Natural asset base", "Biodiversity andEcosystems", "10.  Land resources", "Land cover types, conversions and cover changesState and changes from natural state to artificial or man-made state", "Land use changes since 1990 in the OECD and the world: arable andpermanent crop land; permanent pastures; forest land, and other land,including inland waters and built-up areas.Examples of net conversion of agricultural to other land uses in selected countries aregiven as complements, as well as land cover changes for 2000-2006, and the share ofland sealed by urban and infrastructure development in Europe.", "3"}, 
            {"Natural asset base", "Biodiversity andEcosystems", "10.  Land resources", "Land use: state and changes", "NOT PRESENTED ", "3"}, 
            {"Natural asset base", "Biodiversity andEcosystems", "11. Soil resources", "Degree of top soil losses on agricultural land, other land", "NOT PRESENTED ", "3"}, 
            {"Natural asset base", "Biodiversity andEcosystems", "11. Soil resources", "Agricultural land area affected by water erosion by class of erosion", "NOT PRESENTED ", "3"}, 
            {"Natural asset base", "Biodiversity andEcosystems", "12. Wildlife resources (tbd)", " Trends in farmland or forest bird populations or in breeding bird populations", "The state of farmland or forest birds in Europe and the United States. Birds are seen as good “indicator species” for the integrity of ecosystems and biological diversity. Being close to or at the top of the food chain, they reflect changes inecosystems rather rapidly compared to other species.", "2"}, 
            {"Natural asset base", "Biodiversity andEcosystems", "12. Wildlife resources (tbd)", " Species threat status: mammals, birds, fish, vascular plants in % species assessed or known", "The number of threatened species compared to the number of known or assessed species. Data cover mammals, birds, and vascular plants.", "2"}, 
            {"Natural asset base", "Biodiversity andEcosystems", "12. Wildlife resources (tbd)", " Trends in species abundance", "NOT PRESENTED ", "2"}, 
            {"Environmental quality of life", "Environmentalhealth and risks", "  13.Environmentally induced health problems & related costs", "( years of healthy life lost from degraded environmental conditions)", "NOT PRESENTED ", "27"}, 
            {"Environmental quality of life", "Environmentalhealth and risks", "  13.Environmentally induced health problems & related costs", "Population exposure to air pollution", "Population exposure to air pollution expressed as population weighted concentrations of fine particulates and of ozone in selected European countries. Fine particulates (PM10) can be carried deep into the lungs where they can cause inflammation and worsen the condition of people with heart and lung diseases. Ozone (O3) is a photochemical oxidant, which causes serious health problems and damage to ecosystems, agricultural crops and materials. Humanexposure to high O3 concentrations can give rise to respiratory problems and decreases in lung functions. Ground-level O3 is a ‘secondary’ pollutant: it forms when precursor gases (nitrogen oxides, volatile organic compounds, carbonmonoxide, methane) come into contact in the presence of sunlight.", "27"}, 
            {"Environmental quality of life", "Environmentalhealth and risks", "14. Exposure to natural or industrial risks and related economic losses ", "", "", "15"}, 
            {"Environmental quality of life", "Environmentalservices andamenities", "15. Access to sewage treatment and drinking water", "15.1. Population connected to sewage treatment(at least secondary, in relation to optimal connection rate)", "Public access to waste water treatment services. It shows the percentage of the national resident population actually connected to waste water treatment plants and to sewerage in OECD countries in 2008 or the latest available year.The extent of secondary (biological) and/or tertiary (chemical) treatment provides an indication of efforts to reduce pollution loads.", "7"}, 
            {"Environmental quality of life", "Environmentalservices andamenities", "15. Access to sewage treatment and drinking water", "15.2. Population with sustainable access to safe drinking water", "Indicators on public access to an improved drinking water source, and to an improved sanitation facility, used to monitor the Millennium Development Goals (MDG), are given as complements.", "1"}, 
            {"Economic opportunities and policy responses", "Technology and innovation", "16. R&D expenditure of importance to GG", "R&D expenditure of importance to GG", "R&D expenditure in public and business sector of importance to green growth in energy- and environment-related technologies, expressed in % of all-purpose R&D expenditures.  ", "13"}, 
            {"Economic opportunities and policy responses", "Technology and innovation", "16. R&D expenditure of importance to GG", "Renewable energy (in % of energy related R&D)", "R&D expenditure in public and business sector of importance to green growth in energy", "5"}, 
            {"Economic opportunities and policy responses", "Technology and innovation", "16. R&D expenditure of importance to GG", " Environmental technologies (in % of total R&D, by type)", "R&D expenditure in public and business sector of importance to green growth in  environment-related technologies     ", "13"}, 
            {"Economic opportunities and policy responses", "Technology and innovation", "16. R&D expenditure of importance to GG", " All purpose business R&D (in % of total R&D)", "R&D expenditure in public and business sector of importance to green growth Expressed in % of all-purpose R&D expenditures.    ", "14"}, 
            {"Economic opportunities and policy responses", "Technology and innovation", "17.  Patents of importance to GG", " Patents of importance to GGin % of country applications under the Patent Cooperation Treaty", "Patents of importance to green growth with a focus on (i) patents in energy and climate change mitigation technologies and (ii) patents in pollution abatement and waste management technologies, expressed in number of applicationsunder the Patent Cooperation Treaty (PCT).", "13"}, 
            {"Economic opportunities and policy responses", "Technology and innovation", "17.  Patents of importance to GG", " Environmentally related and all-purpose patents", "", "13"}, 
            {"Economic opportunities and policy responses", "Technology and innovation", "17.  Patents of importance to GG", "Structure of environmentally related patents", "", "13"}, 
            {"Economic opportunities and policy responses", "Technology and innovation", "18. Environment-related innovation in all sectors", "Environment-related innovation in all sectors", "System innovation in all sectors of importance to green growth in terms of firms’ environment-related innovation procedures and underlyingdeterminants, expressed in % of all innovating and manufacturing enterprises (illustrative example for selected EU countries).", "13"}, 
            {"Economic opportunities and policy responses", "Environmentalgoods and services", "19. Production of environmental goods and services (EGS)", "Production of environmental goods and services (EGS)", "Share of “green” enterprises in the economy, expressed as a % of the total number of enterprises. The sectors covered include: retreating (ISIC 25.12); recycling  (ISIC 37); collection, purification and distribution of water (ISIC 41).", "8"}, 
            {"Economic opportunities and policy responses", "Environmentalgoods and services", "19. Production of environmental goods and services (EGS)", "19.1. Gross value added in the EGS sector (in % of GDP)", "", "8"}, 
            {"Economic opportunities and policy responses", "Environmentalgoods and services", "19. Production of environmental goods and services (EGS)", "19.2. Employment in the EGS sector (in % of total employment)", "Employment in the environmental products sector, for selected countries and selected sectors, expressed as a % of the total employment. The sectors covered include: recycling (ISIC 37); collection, purification and distribution of water(ISIC 41); sewage and refuse disposal, sanitation and similar activities (ISIC 90).", "12"}, 
            {"Economic opportunities and policy responses", "Internationalfinancial flows", "20. International financial flows of importance to GG(in % of total flows; in % of GNI)", "International financial flows of importance to GG(in % of total flows; in % of GNI)", "NOT PRESENTED ", "9"}, 
            {"Economic opportunities and policy responses", "Internationalfinancial flows", "20. International financial flows of importance to GG(in % of total flows; in % of GNI)", "20.1. Official Development Assistance", "Total Official Development Assistance (ODA), expressed in % of gross national income; and the share of ODA to environment and renewable energy, expressed in US dollars and in % of total ODA. Official Development Assistance targeting the objectives of the Rio Conventions including biodiversity related aid, desertification related aid and climate change related aid, expressed in US dollars.", "9"}, 
            {"Economic opportunities and policy responses", "Internationalfinancial flows", "20. International financial flows of importance to GG(in % of total flows; in % of GNI)", "20.2. Carbon market financing", "Trading of carbon allowance in terms of value of offset transactions based on known volumes of sales of units and estimates of average offset prices, expressed in US dollars and GtCO2. The structure of supply and demand of certified emissions reductions (CER) credits issued by the Kyoto Proto col’s Clean Development Mechanism (CDM)  projects in the pipeline, expressed in % of all projects by regions and sectors. Private finance here comprises the flows generated through allocation of carbon market allowances, with a focus on CDM projects.", "9"}, 
            {"Economic opportunities and policy responses", "Internationalfinancial flows", "20. International financial flows of importance to GG(in % of total flows; in % of GNI)", "20.3. Foreign Direct Investment (tbd)", " NOT PRESENTED ", "9"}, 
            {"Economic opportunities and policy responses", "Prices andtransfers", "         21.Environmentally related taxation", "Environmentally related taxation", "", "10"}, 
            {"Economic opportunities and policy responses", "Prices andtransfers", "         21.Environmentally related taxation", "Level of environmentally related tax revenues(in % of total tax revenues, in relation to labour related taxes)", "Environmentally related tax revenues expressed in % of total tax revenues, compared to labour tax revenues in % of total tax revenues.", "10"}, 
            {"Economic opportunities and policy responses", "Prices andtransfers", "         21.Environmentally related taxation", "Structure of environmentally related taxes (by type of tax base) Energy pricing", "Taxes and prices for road fuels (diesel, unleaded petrol) and end-use prices for light fuel oil, electricity and natural gas and for industry and households.", "10"}, 
            {"Economic opportunities and policy responses", "Prices andtransfers", "22. Energy pricing", " Energy pricing(share of taxes in end-use prices)", "", "10"}, 
            {"Economic opportunities and policy responses", "Prices andtransfers", "23. Water pricing and cost recovery (tbd)", "Water pricing and cost recovery (tbd)", "", "10"}, 
            {"Economic opportunities and policy responses", "Prices andtransfers", "23. Water pricing and cost recovery (tbd)", " Environmentally related subsidies (tbd)", "", "10"}, 
            {"Economic opportunities and policy responses", "Prices andtransfers", "23. Water pricing and cost recovery (tbd)", "Environmental expenditure: level and structure(pollution abatement and control, biodiversity, natural resource use &management)", "", "10"}, 
            };

        for (int i = 0; i < oecdIndicators.length; i++) {
            String[] indicatorStrings = oecdIndicators[i];
            Indicator oecdIndicator = new Indicator();
            oecdIndicator.setCreator(defaultUser);
            oecdIndicator.setCategory(indicatorStrings[0]);
            oecdIndicator.setSubcategory(indicatorStrings[1]);
            String compoundName = indicatorStrings[2] + ": " + indicatorStrings[3];
            oecdIndicator.setName(compoundName);
            oecdIndicator.setDescription(indicatorStrings[4]);
            Long subdomainId = new Long(indicatorStrings[5]);
            List<Subdomain> subdomains = new ArrayList<Subdomain>();
            Subdomain subdomain = Subdomain.find.byId(subdomainId);
            subdomains.add(subdomain);
            oecdIndicator.setSubdomains(subdomains);
            oecdIndicator.setIndicatorSet(oecdIndicatorSet);
            oecdIndicator.setSource("OECD Green Growth");
            oecdIndicator.save();
        }

        oecdIndicatorSet.save();

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

//        List<Indicator> indicators = indSetOther.getIndicators();
//        indicators.add(waterConsumption);
//        indicators.add(rainAmountInd);
//        indicators.add(bushfireInd);
//        indicators.add(waterSupplyInd);
//        indicators.add(waterConsumptionPerPersonInd);
//        indicators.add(numberDamsInd);
//        indicators.add(numberCrimesInd);
//        indSetOther.setIndicators(indicators);
//        indSetOther.save();
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
