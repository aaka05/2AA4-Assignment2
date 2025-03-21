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
        if (landDetector.foundGround(command)) {
            drone.stop();
    
        }
        
        if(echo){
            sensor.echoForward();
            echo = false;
        }
        else if(turnRight){
            drone.turnRight();
            turnRight = false;
            turnLeft = true;
            echo = true;
        }
        else if(turnLeft){
            drone.turnLeft();
            turnLeft = false;
            turnRight = true;
            echo = true;
        }
        return this;
    }


}
