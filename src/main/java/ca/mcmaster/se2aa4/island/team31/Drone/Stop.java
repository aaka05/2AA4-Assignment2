package ca.mcmaster.se2aa4.island.team31.Drone;
import org.json.JSONObject;

import ca.mcmaster.se2aa4.island.team31.DroneController;
import ca.mcmaster.se2aa4.island.team31.Interfaces.DroneAction;

public class Stop implements DroneAction {

    private DroneController controls;

    public Stop(DroneController controls){
        this.controls = controls;
    }


    @Override
    public JSONObject action() {
        JSONObject decision = new JSONObject();
        decision.put("action", "stop");

        controls.addAction(decision);
        return decision;
    }
}
