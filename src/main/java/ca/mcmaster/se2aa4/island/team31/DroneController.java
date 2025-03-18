package ca.mcmaster.se2aa4.island.team31;

//so this class will be responsible for controlling the drone
import java.util.LinkedList;
import java.util.Queue;
import org.json.JSONObject;

import ca.mcmaster.se2aa4.island.team31.Drone.Battery;

public class DroneController{
    private Direction direction;
    int level;
    int x;
    int y;

    public DroneController(Integer level, String start){
        this.level = level;
        try{
            this.direction = Direction.valueOf(start);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid direction: " + Starting);

        }
    }

    public void addAction(JSONObject decision) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'addAction'");
    }
    
}
