package ca.mcmaster.se2aa4.island.team31.Terrain;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;

import ca.mcmaster.se2aa4.island.team31.Drone.Sensor;
import ca.mcmaster.se2aa4.island.team31.Interfaces.Actions;

public class FindIsland extends State {

    private static final Logger logger = LogManager.getLogger(FindIsland.class);

    private final LandDetector landDetector = new LandDetector();
    
    //these control our zig-zag search pattern
    private boolean turnRight;
    private boolean turnLeft;
    private boolean echo;

    public FindIsland(Actions drone, Sensor sensor, Report report) {
        super(drone, sensor, report);
        this.turnRight = true;
        this.turnLeft = false;
        this.echo = false;
    }

    /**
     * Makes the drone move in a zig-zag pattern to find the island.
     * alternates between moving diagonally right and left while checking for land.
     */
    @Override
    public State getNextState(JSONObject command) {
        if (landDetector.foundGround(command)) {
            logger.info("Found land! Switching to GoToIsland state");
            int distance = landDetector.getDistance(command);
            return new GoToIsland(this.drone, this.sensor, this.report, distance);
        }

        if (echo) {
            sensor.echoForward();
            echo = false;
        } else if (turnRight) {
            drone.turnRight();  // diagonal down-right
            turnRight = false;
            turnLeft = true;
            echo = true;
        } else if (turnLeft) {
            drone.turnLeft();   // diagonal down-left
            turnLeft = false;
            turnRight = true;
            echo = true;
        }

        return this;
    }
}
