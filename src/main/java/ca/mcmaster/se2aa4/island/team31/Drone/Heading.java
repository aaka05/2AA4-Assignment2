package ca.mcmaster.se2aa4.island.team31.Drone;

import org.json.JSONObject;

import ca.mcmaster.se2aa4.island.team31.Direction;
import ca.mcmaster.se2aa4.island.team31.DroneController;
import ca.mcmaster.se2aa4.island.team31.Interfaces.DroneAction;

public class Heading implements DroneAction {

    private DroneController controls;
    private Direction direction;

    public Heading(DroneController controls, Direction direction){
            this.controls = controls;
            this.direction = direction;
    }

    @Override
    public JSONObject action(){
        JSONObject decision = new JSONObject();
        JSONObject parameters = new JSONObject();

        decision.put("action", "heading");
        decision.put("direction", direction);
        decision.put("parameters", parameters);

        controls.addAction(decision);

        return decision;
    }


}
