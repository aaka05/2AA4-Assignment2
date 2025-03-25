package ca.mcmaster.se2aa4.island.team31.SearchStates;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import ca.mcmaster.se2aa4.island.team31.Report;
import ca.mcmaster.se2aa4.island.team31.AbstractClasses.SearchStates;
import ca.mcmaster.se2aa4.island.team31.Detection.GroundSensor;
import ca.mcmaster.se2aa4.island.team31.Drone.Sensor;
import ca.mcmaster.se2aa4.island.team31.Interfaces.Actions;
import ca.mcmaster.se2aa4.island.team31.SearchStates.HelperClasses.MakeTurn;

//state for exploring the island
public class OnIslandState extends SearchStates {
    private static final Logger logger = LogManager.getLogger(OnIslandState.class);
    private final GroundSensor detector;  

    //control flags
    private boolean needToScan;      
    private boolean needToFly;       
    private boolean wasOverLand;     
    private boolean leavingIsland;   

    //counters
    private Integer scannedSteps;    
    private Integer repeatedVisits;  

    public OnIslandState(Actions drone, Sensor sensor, Report report) {
        super(drone, sensor, report);
        detector = new GroundSensor();
        
        
        needToScan = true;
        needToFly = false;
        wasOverLand = false;
        leavingIsland = false;
        scannedSteps = 0;
        repeatedVisits = 0;

        logger.info("Started island exploration");
    }

    @Override
    public SearchStates getNextSearch(JSONObject response) {
        if (leavingIsland) {
            return tryToLeaveIsland(response);  
        }

        if (needToFly) {
            return updateDronePosition(response);  
        } 
        
        if (needToScan) {
            return scanCurrentLocation();  
        }

        return this;
    }

    private SearchStates tryToLeaveIsland(JSONObject response) {
        if (detector.foundGround(response)) {
            return new ApproachIslandState(drone, sensor, report, detector.getDistance(response));
        }
        
        //check if stuck
        if (repeatedVisits > (int)(0.6 * scannedSteps)) {
            return new IslandRelocationState(drone, sensor, report);
        }
        return new MakeTurn(drone, sensor, report);
    }

    private SearchStates updateDronePosition(JSONObject response) {
        checkForPOIs(response); 
        
        //check land-to-ocean transition
        if (isOverOcean(response) && wasOverLand) {
            leavingIsland = true;
            logger.info("Leaving island boundary");
            sensor.echoForward();
            return this;
        }
        
        //update position
        if (!isOverOcean(response) && !wasOverLand) {
            wasOverLand = true;
        } else {
            drone.moveForward();
            needToFly = false;
            needToScan = true;
        }
        return this;
    }

    private SearchStates scanCurrentLocation() {
        scannedSteps++;
        logger.info("Performing scan");

        if (drone.isTurnPoint()) {
            return new IslandRelocationState(drone, sensor, report);
        }

        if (drone.hasVisitedLocation()) {
            repeatedVisits++;
            drone.moveForward();
            wasOverLand = true;
        } else {
            sensor.scan();
            needToFly = true;
            needToScan = false;
        }
        return this;
    }

    private void checkForPOIs(JSONObject response) {
        //check for creeks
        if (hasCreeks(response)) {
            logger.info("Found creek(s)");
            String[] creeks = extractCreeks(response);
            for (String creek : creeks) {
                report.addCreek(creek, drone.getX(), drone.getY());
            }
        }

        //check for emergency sites
        if (hasSites(response)) {
            logger.info("Found emergency site(s)");
            String[] sites = extractSites(response);
            for (String site : sites) {
                report.addSite(site, drone.getX(), drone.getY());
            }       
        }
    }

    private boolean isOverOcean(JSONObject response) {
        JSONObject extras = response.optJSONObject("extras");
        if (extras == null || !extras.has("biomes")) return false;

        JSONArray biomes = extras.optJSONArray("biomes");
        return biomes != null && biomes.length() == 1 && "OCEAN".equals(biomes.optString(0));
    }

    private boolean hasCreeks(JSONObject response) {
        JSONObject extras = response.optJSONObject("extras");
        if (extras == null || !extras.has("creeks")) return false;
        return extras.getJSONArray("creeks").length() > 0;
    }

    private boolean hasSites(JSONObject response) {
        JSONObject extras = response.optJSONObject("extras");
        if (extras == null || !extras.has("sites")) return false;
        return extras.getJSONArray("sites").length() > 0;
    }

    private String[] extractCreeks(JSONObject response) {
        JSONObject extras = response.optJSONObject("extras");
        if (extras != null && extras.has("creeks")) {
            JSONArray creeksArray = extras.getJSONArray("creeks");
            String[] creeks = new String[creeksArray.length()];
            for (int i = 0; i < creeksArray.length(); i++) {
                creeks[i] = creeksArray.getString(i);
            }
            return creeks;
        }
        return new String[0];
    }

    private String[] extractSites(JSONObject response) {
        JSONObject extras = response.optJSONObject("extras");
        if (extras != null && extras.has("sites")) {
            JSONArray sitesArray = extras.getJSONArray("sites");
            String[] sites = new String[sitesArray.length()];
            for (int i = 0; i < sitesArray.length(); i++) {
                sites[i] = sitesArray.getString(i);
            }
            return sites;
        }
        return new String[0];
    }
}








