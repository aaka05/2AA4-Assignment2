package ca.mcmaster.se2aa4.island.team31.Terrain;

import org.json.JSONObject;

import ca.mcmaster.se2aa4.island.team31.Drone.Sensor;
import ca.mcmaster.se2aa4.island.team31.Interfaces.Actions;

public class FindIsland extends State {

    private final LandDetector landDetector = new LandDetector();
    private boolean turnRight;
    private boolean turnLeft;
    private boolean echo;
    // private boolean moveForward;

    public FindIsland(Actions drone, Sensor sensor) {
        super(drone, sensor);
        this.turnRight = true;
        this.turnLeft = false;
        this.echo = false;
    }

    @Override
    public State getNextState(JSONObject command) {
        // first check if we found the island
        if (landDetector.foundGround(command)) {
            // return new OnIsland(drone, sensor);
        }
        
        // do our zigzag pattern
        if(echo) {
            // check what's in front of us
            sensor.echoForward();
            echo = false;
        }
        else if(turnRight) {
            // when we turn right, we move diagonally down-right
            drone.turnRight();  // this actually moves us diagonally
            turnRight = false;
            turnLeft = true;
            echo = true;        // echo after each turn
        }
        else if(turnLeft) {
            // when we turn left, we move diagonally down-left
            drone.turnLeft();   // this actually moves us diagonally
            turnLeft = false;
            turnRight = true;
            echo = true;        // echo after each turn
        }
        
        return this;
    }


}
