package ca.mcmaster.se2aa4.island.team31.Terrain;
 
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;
 
import ca.mcmaster.se2aa4.island.team31.Drone.Sensor;
import ca.mcmaster.se2aa4.island.team31.Enums.Direction;
import ca.mcmaster.se2aa4.island.team31.Interfaces.Actions;
 
public class BackToIsland extends State {
 
    private static final Logger logger = LogManager.getLogger(BackToIsland.class);
 
    private LandDetector landDetector;  // Using LandDetector to check if ground is detected
    private boolean goForward;
    private boolean goLeft;
    private boolean goRight;
    private boolean turnComplete;
    private boolean finalCheck;
   
    public BackToIsland(Actions drone, Sensor sensor, Report report) {
        super(drone, sensor, report);
 
        this.landDetector = new LandDetector();  // Initialize LandDetector
 
        goForward = false;
        goLeft = false;
        goRight = false;
        turnComplete = false;
        finalCheck = false;
 
        logger.info("** In Back To Island State");
    }
 
    /*
     * After we finish searching in one direction, we need to turn the drone around
     * to search the spots we missed. This helps us cover the whole island by going
     * back and forth in a grid pattern. Kind of like mowing a lawn but in both
     * directions to make sure we don't miss anything.
     */
    @Override
    public State getNextState(JSONObject response) {
        if (finalCheck) {
            if (landDetector.foundGround(response)) {
                // If ground is found, move to the island
                return new GoToIsland(this.drone, this.sensor, this.report, landDetector.getDistance(response));
            }
            // If ground is not found, re-locate the island
            return new ReFindIsland(this.drone, this.sensor, this.report);
        }
 
        /*
         * Turn Sequence:
         * Turn towards the island, fly to offset from previous grid search,
         * Turn again in opposite direction to slot into unvisited row/column
         */
 
        if (turnComplete) {
            sensor.echoForward();  // check what's in front of drone after turning
            finalCheck = true;
            return this;
        }
 
        if (goForward) {
            drone.moveForward();  // need to offset by one square to avoid old path
            goForward = false;
            return this;
        }
 
        if (goRight) {
            drone.turnRight();  // first part of the turn sequence
            turnComplete = true;
            return this;
        } else if (goLeft) {
            drone.turnLeft();  // same thing but turning left instead
            turnComplete = true;
            return this;
        }
 
        // Decision making based on the current direction and search pattern
        if (drone.getSearchHeading() == Direction.CardinalDirection.S || drone.getSearchHeading() == Direction.CardinalDirection.E) {
            if (drone.getDirection() == Direction.CardinalDirection.N || drone.getDirection() == Direction.CardinalDirection.E) { // LFR (Left, Forward, Right)
                drone.turnLeft();
                goForward = true;
                goRight = true;
                return this;
            } else if (drone.getDirection() == Direction.CardinalDirection.S || drone.getDirection() == Direction.CardinalDirection.W) { // RFL (Right, Forward, Left)
                drone.turnRight();
                goForward = true;
                goLeft = true;
                return this;
            }
        } else if (drone.getSearchHeading() == Direction.CardinalDirection.N || drone.getSearchHeading() == Direction.CardinalDirection.W) {
            if (drone.getDirection() == Direction.CardinalDirection.S || drone.getDirection() == Direction.CardinalDirection.W) { // LFR (Left, Forward, Right)
                drone.turnLeft();
                goForward = true;
                goRight = true;
                return this;
            } else if (drone.getDirection() == Direction.CardinalDirection.N || drone.getDirection() == Direction.CardinalDirection.E) { // RFL (Right, Forward, Left)
                drone.turnRight();
                goForward = true;
                goLeft = true;
                return this;
            }
        }
 
        return null;  //if no valid transition happens, stay in the current state
    }
}
 