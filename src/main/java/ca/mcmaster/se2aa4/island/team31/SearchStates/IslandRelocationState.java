package ca.mcmaster.se2aa4.island.team31.SearchStates;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;

import ca.mcmaster.se2aa4.island.team31.Report;
import ca.mcmaster.se2aa4.island.team31.AbstractClasses.SearchStates;
import ca.mcmaster.se2aa4.island.team31.Detection.GroundSensor;
import ca.mcmaster.se2aa4.island.team31.Drone.Sensor;
import ca.mcmaster.se2aa4.island.team31.Interfaces.Actions;

/**
 * Represents a state where the drone needs to relocate a lost island.
 */
public class IslandRelocationState extends SearchStates {
    private static final Logger logger = LogManager.getLogger(IslandRelocationState.class);
    private final GroundSensor landDetector;

    //search pattern state flags
    private boolean scanningRight;    
    private boolean scanningLeft;    
    private boolean processingRight;  
    private boolean processingLeft;   
    private boolean hasCompletedTurn; 
    private boolean performingFinalCheck; 

    public IslandRelocationState(Actions drone, Sensor sensor, Report report) {
        super(drone, sensor, report);
        this.landDetector = new GroundSensor();
        initializeSearchPattern();
        logger.info("** Starting island relocation sequence");
    }

    //initialize search pattern
    private void initializeSearchPattern() {
        scanningRight = true;      
        scanningLeft = false;
        processingRight = false;
        processingLeft = false;
        hasCompletedTurn = false;
        performingFinalCheck = false;
    }

    @Override
    public SearchStates getNextSearch(JSONObject response) {
        //state machine implementation for search pattern
        if (performingFinalCheck) {
            return handleFinalCheck(response);
        }

        if (hasCompletedTurn) {
            return performForwardScan();
        }

        if (processingRight || processingLeft) {
            return processDirectionalScanResults(response);
        }

        if (scanningRight || scanningLeft) {
            return executeDirectionalScan();
        }

        //stop and wait for next command
        drone.stop();
        return this;
    }

    //handles the final forward check after turning towards potential land
    private SearchStates handleFinalCheck(JSONObject response) {
        if (landDetector.foundGround(response)) {
            logger.info("** Land detected, initiating approach");
            return new ApproachIslandState(this.drone, this.sensor, this.report, 
                                landDetector.getDistance(response));
        }
        logger.info("** No land found, restarting search pattern");
        return new IslandRelocationState(this.drone, this.sensor, this.report);
    }

    //initiates forward scan after completing turn towards potential land
    private SearchStates performForwardScan() {
        sensor.echoForward();
        performingFinalCheck = true;
        hasCompletedTurn = false;
        return this;
    }

    //processes the results of directional scans and initiates turn if land is detected
    private SearchStates processDirectionalScanResults(JSONObject response) {
        if (processingRight && landDetector.foundGround(response)) {
            logger.info("** Land detected to the right");
            drone.turnRight();
            hasCompletedTurn = true;
            processingRight = false;
            return this;
        } else if (processingLeft && landDetector.foundGround(response)) {
            logger.info("** Land detected to the left");
            drone.turnLeft();
            hasCompletedTurn = true;
            processingLeft = false;
            return this;
        }
        
        //reset flags if no land detected
        processingRight = processingLeft = false;
        return this;
    }

    //executes the directional scanning sequence (right then left)
    private SearchStates executeDirectionalScan() {
        if (scanningRight) {
            logger.info("** Scanning right");
            sensor.echoRight();
            processingRight = true;
            scanningLeft = true;
            scanningRight = false;
            return this;
        } else if (scanningLeft) {
            logger.info("** Scanning left");
            sensor.echoLeft();
            scanningRight = false;
            processingLeft = true;
            scanningLeft = false;
            return this;
        }
        return this;
    }
}