package ca.mcmaster.se2aa4.island.team31.Terrain;
 
 
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;

import ca.mcmaster.se2aa4.island.team31.Drone.Sensor;
import ca.mcmaster.se2aa4.island.team31.Interfaces.Actions;
 
 
public class ReFindIsland extends State {
 
 
    private static final Logger logger = LogManager.getLogger(ReFindIsland.class);
 
 
    private LandDetector landDetector;
    private boolean echoRight;
    private boolean echoLeft;
    private boolean checkRight;
    private boolean checkLeft;
    private boolean turnComplete;
    private boolean finalCheck;
   
    public ReFindIsland(Actions drone, Sensor sensor, Report report) {
        super(drone, sensor, report);
        this.landDetector = new LandDetector();
       
        echoRight = true;
        echoLeft = false;
        checkRight = false;
        checkLeft = false;
        turnComplete = false;
        finalCheck = false;
 
 
        logger.info("** In ReFind Island State");
    }
 
 
    @Override
    public State getNextState(JSONObject response) {
        if (finalCheck) { // if ground found then go to it
            if (landDetector.foundGround(response)) {
                return new GoToIsland(this.drone, this.sensor, this.report, landDetector.getDistance(response));
            }
            // otherwise repeat process in new orientation
            return new ReFindIsland(this.drone, this.sensor, this.report);
        }
 
 
        // If we are turning, handle the echo and check ground
        if (turnComplete) {
            sensor.echoForward();
            finalCheck = true;
            turnComplete = false;
            return this;
        }
 
 
        // Check right first
        if (checkRight) {
            if (landDetector.foundGround(response)) {
                drone.turnRight();
                turnComplete = true;
                return this;
            }
            checkRight = false;
        } else if (checkLeft) {
            if (landDetector.foundGround(response)) {
                drone.turnLeft();
                turnComplete = true;
                return this;
            }
            checkLeft = false;
        }
 
 
        // Echo to the right or left to search for land
        if (echoRight) {
            sensor.echoRight();
            checkRight = true;
            echoLeft = true;
            echoRight = false;
            return this;
        } else if (echoLeft) {
            sensor.echoLeft();
            echoRight = false;
            checkLeft = true;
            echoLeft = false;
            return this;
        }
 
 
        drone.stop();
        return this;
    }
}