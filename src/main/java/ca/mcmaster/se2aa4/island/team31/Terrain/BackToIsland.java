package ca.mcmaster.se2aa4.island.team31.Terrain;
 
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;
 
import ca.mcmaster.se2aa4.island.team31.Drone.Sensor;
import ca.mcmaster.se2aa4.island.team31.Interfaces.Actions;
import ca.mcmaster.se2aa4.island.team31.Enums.Direction;
 
public class BackToIsland extends State {
 
    private static final Logger logger = LogManager.getLogger(BackToIsland.class);
 
    private LandDetector landDetector;  // Using LandDetector to check if ground is detected
    private boolean goForward;
    private boolean goLeft;
    private boolean goRight;
    private boolean turnComplete;
    private boolean finalCheck;
   
    public BackToIsland(Actions drone, Sensor sensor) {
        super(drone, sensor);
 
        this.landDetector = new LandDetector();  // Initialize LandDetector
 
        goForward = false;
        goLeft = false;
        goRight = false;
        turnComplete = false;
        finalCheck = false;
 
        logger.info("** In Back To Island State");
    }
 
    /*
     * After completing a grid search of the island in one direction, the drone will turn to prepare it to
     * complete a grid search in the opposite direction, visiting and scanning the missed rows/columns
     * to ensure a thorough search.
     */
    @Override
    public State getNextState(JSONObject response) {
        if (finalCheck) {
            if (landDetector.foundGround(response)) {
                // If ground is found, move to the island
                return new GoToIsland(this.drone, this.sensor, landDetector.getDistance(response));
            }
            // If ground is not found, re-locate the island
            return new ReFindIsland(this.drone, this.sensor);
        }
 
        /*
         * Turn Sequence:
         * Turn towards the island, fly to offset from previous grid search,
         * Turn again in opposite direction to slot into unvisited row/column
         */
 
        if (turnComplete) {
            sensor.echoForward();  // Perform the echo after turning
            finalCheck = true;
            return this;
        }
 
        if (goForward) {
            drone.moveForward();  // Move forward to the next grid square
            goForward = false;
            return this;
        }
 
        if (goRight) {
            drone.turnRight();  // Turn right to face the next quadrant
            turnComplete = true;
            return this;
        } else if (goLeft) {
            drone.turnLeft();  // Turn left to face the next quadrant
            turnComplete = true;
            return this;
        }
 
        // Decision making based on the current direction and search pattern
        if (drone.getDirection() == Direction.CardinalDirection.S || drone.getDirection() == Direction.CardinalDirection.E) {
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
        } else if (drone.getDirection() == Direction.CardinalDirection.N || drone.getDirection() == Direction.CardinalDirection.W) {
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
 
        return this;  // If no valid transition happens, stay in the current state
    }
}
 