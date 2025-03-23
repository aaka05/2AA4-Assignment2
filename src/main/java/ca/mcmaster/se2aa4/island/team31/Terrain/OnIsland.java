package ca.mcmaster.se2aa4.island.team31.Terrain;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import ca.mcmaster.se2aa4.island.team31.Drone.Sensor;
import ca.mcmaster.se2aa4.island.team31.Interfaces.Actions;

public class OnIsland extends State {

    private static final Logger logger = LogManager.getLogger(OnIsland.class);

    private final LandDetector landDetector;

    private boolean shouldScan;
    private boolean shouldFly;
    private boolean prevFoundLand;

    private boolean exitingIsland;

    private Integer totalScannedSteps;
    private Integer revisitedLocationsCount;


    public OnIsland(Actions drone, Sensor sensor) {
        super(drone, sensor);
        shouldScan = true;
        shouldFly = false;
        prevFoundLand = false;

        exitingIsland = false;
        totalScannedSteps = 0;
        revisitedLocationsCount = 0;
        

        this.landDetector = new LandDetector();

        logger.info("** In Search State");


    }

    @Override
    public State getNextState(JSONObject response) {

        if(exitingIsland){
            if (landDetector.foundGround(response)){
                return new GoToIsland(this.drone, this.sensor, landDetector.getDistance(response));
            }
            else{
                if(revisitedLocationsCount > (int)(0*6 * totalScannedSteps)){
                    return new ReFindIsland(this.drone, this.sensor);
                 }
                return new MakeTurn(this.drone, this.sensor);
            }
        }

        if (shouldFly){
            if (foundCreek(response)) {
                logger.info("** Creek Found!");
                String[] creeks = getCreeks(response);
                // adding creeks to report
                //for (String creek : creeks) {
                   // addCreekToReport(creek);
                //}
            }

            // checks if emergency site is found from previous scan
            if (foundSite(response)) {
                logger.info("** Emergency Site Found!");
                String[] sites = getSites(response);
                // adding site to report
                //for (String site : sites) {
                    //addSiteToReport(site);
                //}
            }

            if (inOcean(response) && prevFoundLand){
                exitingIsland = true;
                logger.info("**LEFT ISLAND");

                sensor.echoForward();
                return this;
            }
            else if(!inOcean(response) && !prevFoundLand){
                prevFoundLand = true;
            }
            else{
                drone.moveForward();
                shouldFly = false;
                shouldScan = true;
            }
        } else if(shouldScan){
            totalScannedSteps++;
            logger.info("Scanning");

            if(drone.isTurnPoint()){
                return new ReFindIsland(this.drone, this.sensor);
            }

            if (drone.hasVisitedLocation()){
                revisitedLocationsCount++;
                drone.moveForward();
                prevFoundLand = true;
            }else{
                sensor.scan();
                shouldFly = true;
                shouldScan = false;
            }
        }


        return this;
    }

    private boolean inOcean(JSONObject response){
        if (!response.has("extras")){
            return false;
        }
        JSONObject extras = response.getJSONObject("extras");
        if (extras == null || !extras.has("biomes")) return false;

        JSONArray biomes = extras.optJSONArray("biomes");
        return biomes != null && biomes.length() == 1 && "OCEAN".equals(biomes.optString(0));
    }



    private boolean foundCreek(JSONObject response) {
        if (!response.has("extras")) return false;

        JSONObject extras = response.getJSONObject("extras");
        if (!extras.has("creeks")) return false;

        JSONArray creeks = extras.getJSONArray("creeks");
        if (creeks.length() > 0) {
            return true;
        }
        return false;
    }

    private boolean foundSite(JSONObject response) {
        if (!response.has("extras")) return false;

        JSONObject extras = response.getJSONObject("extras");
        if (!extras.has("sites")) return false;

        JSONArray sites = extras.getJSONArray("sites");
        if (sites.length() > 0) {
            return true;
        }
        return false;
    }


    private String[] getCreeks(JSONObject response) {
        JSONObject extras = response.optJSONObject("extras");
        if (extras != null && extras.has("creeks")) {
            JSONArray creeksArray = extras.optJSONArray("creeks");
            String[] creeks = new String[creeksArray.length()];
            for (int i = 0; i < creeksArray.length(); i++) {
                creeks[i] = creeksArray.optString(i);
            }
            return creeks;
        }
        return new String[0]; // return an empty array if no creeks are found
    }
    
    private String[] getSites(JSONObject response) {
        JSONObject extras = response.optJSONObject("extras");
        if (extras != null && extras.has("sites")) {
            JSONArray sitesArray = extras.optJSONArray("sites");
            String[] sites = new String[sitesArray.length()];
            for (int i = 0; i < sitesArray.length(); i++) {
                sites[i] = sitesArray.optString(i);
            }
            return sites;
        }
        return new String[0]; // return an empty array if no sites are found
    }

}





