package ca.mcmaster.se2aa4.island.team31.Terrain;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import ca.mcmaster.se2aa4.island.team31.Drone.Sensor;
import ca.mcmaster.se2aa4.island.team31.Interfaces.Actions;

//state class for when the drone is exploring the island
//handles movement, scanning, and POI detection
public class OnIsland extends State {
    private static final Logger logger = LogManager.getLogger(OnIsland.class);
    private final LandDetector landDetector;

    // Drone behavior flags
    private boolean shouldScan;  //true if drone needs to scan current location
    private boolean shouldFly;   //true if drone should move forward
    private boolean prevFoundLand;  //tracks if we were over land in previous step
    private boolean exitingIsland;  //true when we're trying to leave the island

    private Integer totalScannedSteps;
    private Integer revisitedLocationsCount;

    public OnIsland(Actions drone, Sensor sensor, Report report) {
        super(drone, sensor, report);
        this.landDetector = new LandDetector();
        
        //initialize control flags
        shouldScan = true;
        shouldFly = false;
        prevFoundLand = false;
        exitingIsland = false;

        //initialize counters
        totalScannedSteps = 0;
        revisitedLocationsCount = 0;

        logger.info("** In Search State");
    }

    @Override
    public State getNextState(JSONObject response) {
        //check if we're trying to exit the island
        if (exitingIsland) {
            return handleExitingIsland(response);
        }

        //normal island exploration
        if (shouldFly) {
            return handleFlying(response);
        } else if (shouldScan) {
            return handleScanning();
        }

        return this;
    }

    //helper method for handling island exit logic
    private State handleExitingIsland(JSONObject response) {
        if (landDetector.foundGround(response)) {
            return new GoToIsland(drone, sensor, report, landDetector.getDistance(response));
        }
        
        //if we're revisiting too many locations, we might be stuck
        if (revisitedLocationsCount > (int)(0.6 * totalScannedSteps)) {
            //if we're stuck, find a island again
            return new ReFindIsland(drone, sensor, report);
        }
        return new MakeTurn(drone, sensor, report);
    }

    //handles drone movement and location tracking
    private State handleFlying(JSONObject response) {
        handlePOIDetection(response);
        
        //check if we've crossed from land to ocean
        if (inOcean(response) && prevFoundLand) {
            exitingIsland = true;
            logger.info("**LEFT ISLAND");
            sensor.echoForward();
            return this;
        }
        
        //update land tracking and move forward
        //first time we've seen land
        if (!inOcean(response) && !prevFoundLand) {
            prevFoundLand = true;
        } else {
            //move forward and reset scan flag
            drone.moveForward();
            shouldFly = false;
            shouldScan = true;
        }
        return this;
    }

    //handles scanning logic and visited location tracking
    private State handleScanning() {
        totalScannedSteps++;
        logger.info("Scanning");

        if (drone.isTurnPoint()) {
            return new ReFindIsland(drone, sensor, report);
        }

        //if we've already visited this location, move forward
        if (drone.hasVisitedLocation()) {
            revisitedLocationsCount++;
            drone.moveForward();
            prevFoundLand = true;
        } else {
            //scan and move forward
            sensor.scan();
            shouldFly = true;
            shouldScan = false;
        }
        return this;
    }

    //handles POI detection 
    private void handlePOIDetection(JSONObject response) {
        if (foundCreek(response)) {
            logger.info("** Creek has been Found! Adding to drone report");
            String[] creeks = getCreeks(response);
            for (String creek : creeks) {
                addCreekToReport(creek);
            }
        }

        //emergency site found
        if (foundSite(response)) {
            logger.info("** Emergency Site has been Found! Adding to drone report");
            String[] sites = getSites(response);
            for (String site : sites) {
                addSiteToReport(site);
            }       
        }
    }

    //checks if the drone is over ocean
    private boolean inOcean(JSONObject response) {
        JSONObject extras = response.optJSONObject("extras");
        if (extras == null || !extras.has("biomes")) return false;

        JSONArray biomes = extras.optJSONArray("biomes");
        //if biomes is only ocean, return true
        return biomes != null && biomes.length() == 1 && "OCEAN".equals(biomes.optString(0));
    }

    //checks if the drone is over a creek
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

    //checks if the drone is over an emergency site
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

    //gets the creeks from the response
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

    //gets the emergency sites from the response
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

    //methods to update the report with found POIs
    private void addCreekToReport(String creekId) {
        report.addCreek(creekId, drone.getX(), drone.getY());
    }

    private void addSiteToReport(String siteId) {
        report.addSite(siteId, drone.getX(), drone.getY());
    }  
}








