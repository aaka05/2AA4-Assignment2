package ca.mcmaster.se2aa4.island.team31.Terrain;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;

import ca.mcmaster.se2aa4.island.team31.Drone.Sensor;
import ca.mcmaster.se2aa4.island.team31.Interfaces.Actions;

public class FindIsland extends State {

    private static final Logger logger = LogManager.getLogger(FindIsland.class);

    private final LandDetector landDetector = new LandDetector();
    private boolean turnRight;
    private boolean turnLeft;
    private boolean echo;

    public FindIsland(Actions drone, Sensor sensor) {
        super(drone, sensor);
        this.turnRight = true;
        this.turnLeft = false;
        this.echo = false;
    }

    @Override
    public State getNextState(JSONObject command) {
        //  Check if land was found and switch state
        if (landDetector.foundGround(command)) {
            logger.info("Found land, switching to GoToIsland state");
            int distance = landDetector.getDistance(command);
            return new GoToIsland(drone, sensor, distance); //  Make sure GoToIsland exists
        }

        //  Zig-zag movement pattern with echo
        if (echo) {
            sensor.echoForward();
            echo = false;
        } else if (turnRight) {
            drone.turnRight();  // move diagonally down-right
            turnRight = false;
            turnLeft = true;
            echo = true;
        } else if (turnLeft) {
            drone.turnLeft();   // move diagonally down-left
            turnLeft = false;
            turnRight = true;
            echo = true;
        }

        return this;
    }
}
