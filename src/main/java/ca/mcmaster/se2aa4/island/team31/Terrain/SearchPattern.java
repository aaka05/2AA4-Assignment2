package ca.mcmaster.se2aa4.island.team31.Terrain;

import ca.mcmaster.se2aa4.island.team31.Drone.Sensor;
import ca.mcmaster.se2aa4.island.team31.Interfaces.Actions;

//this class contains the zig-zag search pattern for the drone which
//gets called by the FindIsland state

public class SearchPattern {
    private final Actions drone;
    private final Sensor sensor;
    private boolean shouldEcho;
    private boolean movingRight;

    public SearchPattern(Actions drone, Sensor sensor) {
        this.drone = drone;
        this.sensor = sensor;
        this.shouldEcho = false;
        this.movingRight = true;
    }

    //this method executes the next move in the zig-zag search pattern
    public void executeNextMove() {
        if (shouldEcho) {
            sensor.echoForward();
            shouldEcho = false;
            return;
        }

        if (movingRight) {
            drone.turnRight();  
        } else {
            drone.turnLeft(); 
        }
        
        movingRight = !movingRight;
        shouldEcho = true;
    }
} 