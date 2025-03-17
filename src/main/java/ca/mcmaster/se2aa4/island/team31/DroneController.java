package ca.mcmaster.se2aa4.island.team31;

//so this class will be responsible for controlling the drone
import java.util.LinkedList;
import java.util.Queue;
import org.json.JSONObject;

import ca.mcmaster.se2aa4.island.team31.Drone.Battery;

public class DroneController {
    private Battery battery;
    private Queue<JSONObject> actions;

    public DroneController(Battery battery) {
        this.battery = battery;
        this.actions = new LinkedList<>();
        this.battery = battery;
    }

    public void addAction(JSONObject decision) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'addAction'");
    }
    
}
