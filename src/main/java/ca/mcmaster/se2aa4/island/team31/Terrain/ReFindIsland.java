package ca.mcmaster.se2aa4.island.team31.Terrain;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;

import ca.mcmaster.se2aa4.island.team31.Drone.Sensor;
import ca.mcmaster.se2aa4.island.team31.Interfaces.Actions;

//state class for when the drone needs to relocate the island
//uses a systematic scanning pattern: right -> left -> forward
public class ReFindIsland extends State {
    private static final Logger logger = LogManager.getLogger(ReFindIsland.class);
    private final LandDetector landDetector;

    //scanning phase flags
    private boolean echoRight;    
    private boolean echoLeft;     
    private boolean checkRight;   
    private boolean checkLeft;    
    private boolean turnComplete; 
    private boolean finalCheck;  

    public ReFindIsland(Actions drone, Sensor sensor, Report report) {
        super(drone, sensor, report);
        this.landDetector = new LandDetector();
        
        //initialize scanning sequence
        echoRight = true;     //start by scanning right
        echoLeft = false;
        checkRight = false;
        checkLeft = false;
        turnComplete = false;
        finalCheck = false;

        logger.info("** Starting island relocation sequence");
    }

    @Override
    public State getNextState(JSONObject response) {
        //final check after turning towards potential land
        if (finalCheck) {
            return handleFinalCheck(response);
        }

        //handle post-turn forward scan
        if (turnComplete) {
            return handleTurnComplete();
        }

        //process scan results
        if (checkRight || checkLeft) {
            return handleScanResults(response);
        }

        //perform directional scans
        if (echoRight || echoLeft) {
            return performDirectionalScan();
        }

        //if we reach here, stop and wait for next command
        drone.stop();
        return this;
    }

    //handles the final forward check after turning
    private State handleFinalCheck(JSONObject response) {
        if (landDetector.foundGround(response)) {
            logger.info("** Land detected, initiating approach");
            return new GoToIsland(this.drone, this.sensor, this.report, 
                                landDetector.getDistance(response));
        }
        logger.info("** No land found, restarting search pattern");
        return new ReFindIsland(this.drone, this.sensor, this.report);
    }

    //initiatze forward scan after completing turn
    private State handleTurnComplete() {
        sensor.echoForward();
        finalCheck = true;
        turnComplete = false;
        return this;
    }

    //processes the results of right/left scans
    private State handleScanResults(JSONObject response) {
        if (checkRight) {
            if (landDetector.foundGround(response)) {
                logger.info("** Land detected to the right");
                drone.turnRight();
                turnComplete = true;
                return this;
            }
            checkRight = false;
        } else if (checkLeft) {
            if (landDetector.foundGround(response)) {
                logger.info("** Land detected to the left");
                drone.turnLeft();
                turnComplete = true;
                return this;
            }
            checkLeft = false;
        }
        return this;
    }

    //performs directional scanning sequence
    private State performDirectionalScan() {
        if (echoRight) {
            logger.info("** Scanning right");
            sensor.echoRight();
            checkRight = true;
            echoLeft = true;
            echoRight = false;
            return this;
        } else if (echoLeft) {
            logger.info("** Scanning left");
            sensor.echoLeft();
            echoRight = false;
            checkLeft = true;
            echoLeft = false;
            return this;
        }
        return this;
    }
}