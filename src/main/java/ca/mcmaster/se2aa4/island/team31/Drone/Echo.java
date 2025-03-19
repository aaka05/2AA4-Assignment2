package ca.mcmaster.se2aa4.island.team31.Drone;

import org.json.JSONObject;

import ca.mcmaster.se2aa4.island.team31.Direction;
import ca.mcmaster.se2aa4.island.team31.DroneController;
import ca.mcmaster.se2aa4.island.team31.Interfaces.DroneAction;

public class Echo implements DroneAction{
    private DroneController controls;
    private Direction.CardinalDirection direction;

    public Echo(DroneController controls, Direction.CardinalDirection direction){
        this.controls = controls;
        this.direction = direction;
    }

    public JSONObject action(){
        JSONObject decision = new JSONObject();
        JSONObject parameter = new JSONObject();

        decision.put("action", "echo");
        parameter.put("direction", direction);
        decision.put("parameters", parameter);
        controls.addAction(decision);
        return decision;
    }

}
