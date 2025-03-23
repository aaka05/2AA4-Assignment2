package ca.mcmaster.se2aa4.island.team31.Terrain;
 
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;
 
import ca.mcmaster.se2aa4.island.team31.Drone.Sensor;
import ca.mcmaster.se2aa4.island.team31.Enums.Direction;
import ca.mcmaster.se2aa4.island.team31.Interfaces.Actions;
 
public class BackToIsland extends State {
 
    private static final Logger logger = LogManager.getLogger(BackToIsland.class);
 
    //helper class to check if drone can see ground
    private LandDetector landDetector;
    
    //flags to keep track of what drone needs to do next
    private boolean goForward;
    private boolean goLeft;
    private boolean goRight;
    private boolean turnComplete;
    private boolean finalCheck;
   
    public BackToIsland(Actions drone, Sensor sensor, Report report) {
        super(drone, sensor, report);
 
        //create new detector to help drone find land
        landDetector = new LandDetector();
 
        //initialize all movement flags to false at the start
        goForward = false;
        goLeft = false;
        goRight = false;
        turnComplete = false;
        finalCheck = false;
 
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
        //first, check if drone is done turning
        if (finalCheck) {
            //see if we can spot any land
            if (landDetector.foundGround(response)) {
                //found land, heads towards it
                return new GoToIsland(this.drone, this.sensor, this.report, 
                                    landDetector.getDistance(response));
            }
            //flown off island, need to find it again
            return new ReFindIsland(this.drone, this.sensor, this.report);
        }
 
        //if drone finished turning, do one last check in front of it
        if (turnComplete) {
            sensor.echoForward(); 
            finalCheck = true;
            return this;
        }
 
        //need to move forward one square to avoid where we've already been
        if (goForward) {
            drone.moveForward();
            goForward = false;
            return this;
        }
 
        //handle the actual turning part
        if (goRight) {
            drone.turnRight();
            turnComplete = true;
            return this;
        } else if (goLeft) {
            drone.turnLeft();
            turnComplete = true;
            return this;
        }
 
        //figure out which way to turn
        //if drone is searching south or east:
        if (drone.getSearchHeading() == Direction.CardinalDirection.S || 
            drone.getSearchHeading() == Direction.CardinalDirection.E) {
            
            if (drone.getDirection() == Direction.CardinalDirection.N || 
                drone.getDirection() == Direction.CardinalDirection.E) {
                //do a left-forward-right turn
                drone.turnLeft();
                goForward = true;
                goRight = true;
                return this;
            } else if (drone.getDirection() == Direction.CardinalDirection.S || 
                      drone.getDirection() == Direction.CardinalDirection.W) {
                //do a right-forward-left turn
                drone.turnRight();
                goForward = true;
                goLeft = true;
                return this;
            }
        }
        //if drone is searching north or west:
        else if (drone.getSearchHeading() == Direction.CardinalDirection.N || 
                 drone.getSearchHeading() == Direction.CardinalDirection.W) {
            
            if (drone.getDirection() == Direction.CardinalDirection.S || 
                drone.getDirection() == Direction.CardinalDirection.W) {
                //do a left-forward-right turn
                drone.turnLeft();
                goForward = true;
                goRight = true;
                return this;
            } else if (drone.getDirection() == Direction.CardinalDirection.N || 
                      drone.getDirection() == Direction.CardinalDirection.E) {
                //do a right-forward-left turn
                drone.turnRight();
                goForward = true;
                goLeft = true;
                return this;
            }
        }
 
        //should never reach here - something went wrong
        return null;
    }
}
 