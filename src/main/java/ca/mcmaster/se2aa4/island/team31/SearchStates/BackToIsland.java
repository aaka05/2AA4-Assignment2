package ca.mcmaster.se2aa4.island.team31.SearchStates;
 
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;

import ca.mcmaster.se2aa4.island.team31.Direction;
import ca.mcmaster.se2aa4.island.team31.Report;
import ca.mcmaster.se2aa4.island.team31.AbstractClasses.State;
import ca.mcmaster.se2aa4.island.team31.Detection.GroundSensor;
import ca.mcmaster.se2aa4.island.team31.Drone.Sensor;
import ca.mcmaster.se2aa4.island.team31.Interfaces.Actions;
 
public class BackToIsland extends State {
 
    private static final Logger logger = LogManager.getLogger(BackToIsland.class);
 
    //helper class to check if drone can see ground
    private final GroundSensor landDetector;
    
    //Encapsulate turn states in an enum
    private enum TurnState {
        INITIAL,
        MOVING_FORWARD,
        TURNING_LEFT,
        TURNING_RIGHT,
        TURN_COMPLETE,
        FINAL_CHECK
    }
    
    private TurnState currentState;
   
    public BackToIsland(Actions drone, Sensor sensor, Report report) {
        super(drone, sensor, report);
        landDetector = new GroundSensor();
        currentState = TurnState.INITIAL;
        logger.info("Starting BackToIsland state - trying to get back to the island!");
    }
 
    /**
     * This method figures out where the drone should go next.
     * like mowing a lawn - you go in one direction,
     * then turn around and go back the other way to cover the whole area.
     * 
     * Basic steps are:
     * 1. Check if drone is done turning (finalCheck)
     * 2. Execute turn
     * 3. Figure out which way to turn based on drone's current direction
     */

    @Override
    public State getNextState(JSONObject response) {
        if (currentState == TurnState.FINAL_CHECK) {
            return handleFinalCheck(response);
        }
        
        if (currentState == TurnState.TURN_COMPLETE) {
            sensor.echoForward();
            currentState = TurnState.FINAL_CHECK;
            return this;
        }
        
        if (currentState == TurnState.MOVING_FORWARD) {
            drone.moveForward();
            currentState = TurnState.TURNING_RIGHT;
            return this;
        }
        
        if (currentState == TurnState.TURNING_RIGHT) {
            drone.turnRight();
            currentState = TurnState.TURN_COMPLETE;
            return this;
        }
        
        if (currentState == TurnState.TURNING_LEFT) {
            drone.turnLeft();
            currentState = TurnState.TURN_COMPLETE;
            return this;
        }
        
        return determineTurnDirection();
    }
    
    private State handleFinalCheck(JSONObject response) {
        if (landDetector.foundGround(response)) {
            return new GoToIsland(drone, sensor, report, landDetector.getDistance(response));
        }
        return new ReFindIsland(drone, sensor, report);
    }
    
    private State determineTurnDirection() {
        Direction.CardinalDirection searchHeading = drone.getSearchHeading();
        Direction.CardinalDirection currentDirection = drone.getDirection();
        
        boolean isSearchingSouthOrEast = searchHeading == Direction.CardinalDirection.S || 
                                       searchHeading == Direction.CardinalDirection.E;
        boolean isSearchingNorthOrWest = searchHeading == Direction.CardinalDirection.N || 
                                       searchHeading == Direction.CardinalDirection.W;
                                       
        if (shouldTurnLeft(isSearchingSouthOrEast, isSearchingNorthOrWest, currentDirection)) {
            drone.turnLeft();
            currentState = TurnState.MOVING_FORWARD;
            return this;
        } else if (shouldTurnRight(isSearchingSouthOrEast, isSearchingNorthOrWest, currentDirection)) {
            drone.turnRight();
            currentState = TurnState.MOVING_FORWARD;
            return this;
        }
        
        return null; //should never reach here
    }
    
    private boolean shouldTurnLeft(boolean isSearchingSouthOrEast, boolean isSearchingNorthOrWest, 
                                 Direction.CardinalDirection currentDirection) {
        return (isSearchingSouthOrEast && (currentDirection == Direction.CardinalDirection.N || 
                                         currentDirection == Direction.CardinalDirection.E)) ||
               (isSearchingNorthOrWest && (currentDirection == Direction.CardinalDirection.S || 
                                         currentDirection == Direction.CardinalDirection.W));
    }
    
    private boolean shouldTurnRight(boolean isSearchingSouthOrEast, boolean isSearchingNorthOrWest, 
                                  Direction.CardinalDirection currentDirection) {
        return (isSearchingSouthOrEast && (currentDirection == Direction.CardinalDirection.S || 
                                         currentDirection == Direction.CardinalDirection.W)) ||
               (isSearchingNorthOrWest && (currentDirection == Direction.CardinalDirection.N || 
                                         currentDirection == Direction.CardinalDirection.E));
    }
}
 