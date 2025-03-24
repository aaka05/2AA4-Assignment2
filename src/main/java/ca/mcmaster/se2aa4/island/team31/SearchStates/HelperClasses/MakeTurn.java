package ca.mcmaster.se2aa4.island.team31.SearchStates.HelperClasses;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;

import ca.mcmaster.se2aa4.island.team31.Report;
import ca.mcmaster.se2aa4.island.team31.AbstractClasses.State;
import ca.mcmaster.se2aa4.island.team31.Detection.LandDetector;
import ca.mcmaster.se2aa4.island.team31.Direction.CardinalDirection;
import ca.mcmaster.se2aa4.island.team31.Drone.Sensor;
import ca.mcmaster.se2aa4.island.team31.Interfaces.Actions;
import ca.mcmaster.se2aa4.island.team31.SearchStates.BackToIsland;
import ca.mcmaster.se2aa4.island.team31.SearchStates.GoToIsland;

public class MakeTurn extends State {

    private static final Logger logger = LogManager.getLogger(MakeTurn.class);
    private LandDetector landDetector = new LandDetector();
    
    //state flags for the turn sequence
    private boolean droneHasTurned;
    private boolean checkLand;
    private boolean turnRight;
    private boolean turnLeft;

    public MakeTurn(Actions drone, Sensor sensor, Report report) {
        super(drone, sensor, report);
        turnRight = false;
        turnLeft = false;
        droneHasTurned = false;
        checkLand = false;
        logger.info("Starting turn sequence");
    }

    @Override
    public State getNextState(JSONObject response) {
        //check if we need to look for land
        if (checkLand) {
            if (landDetector.foundGround(response)) {
                return new GoToIsland(this.drone, this.sensor, this.report, 
                                    landDetector.getDistance(response));
            }
            return new BackToIsland(this.drone, this.sensor, this.report);
        }

        // Check if turn is complete
        if (droneHasTurned) {
            checkLand = true;
            sensor.echoForward();
            return this;
        }

        // Execute the turn if direction is set
        if (turnRight) {
            drone.turnRight();
            droneHasTurned = true;
            return this;
        }
        if (turnLeft) {
            drone.turnLeft();
            droneHasTurned = true;
            return this;
        }

        // Determine which way to turn based on search heading
        CardinalDirection searchHeading = drone.getSearchHeading();
        CardinalDirection currentDir = drone.getDirection();
        
        if (searchHeading == CardinalDirection.S || searchHeading == CardinalDirection.E) {
            if (currentDir == CardinalDirection.N || currentDir == CardinalDirection.E) {
                turnRight = true;
                drone.turnRight();
                return this;
            }
            if (currentDir == CardinalDirection.S || currentDir == CardinalDirection.W) {
                turnLeft = true;
                drone.turnLeft();
                return this;
            }
        }
        else if (searchHeading == CardinalDirection.N || searchHeading == CardinalDirection.W) {
            if (currentDir == CardinalDirection.S || currentDir == CardinalDirection.W) {
                turnRight = true;
                drone.turnRight();
                return this;
            }
            if (currentDir == CardinalDirection.N || currentDir == CardinalDirection.E) {
                turnLeft = true;
                drone.turnLeft();
                return this;
            }
        }

        throw new IllegalStateException("Invalid direction combination");
    }
}
