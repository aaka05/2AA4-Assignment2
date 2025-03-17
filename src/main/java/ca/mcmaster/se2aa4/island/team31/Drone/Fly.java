package ca.mcmaster.se2aa4.island.team31.Drone;

import org.json.JSONObject;

import ca.mcmaster.se2aa4.island.team31.DroneController;
import ca.mcmaster.se2aa4.island.team31.Interfaces.DroneAction;

public class Fly implements DroneAction {

    private DroneController controls;

    public Fly(DroneController controls) {
        this.controls = controls;
    }
    @Override
    public JSONObject action() {
        JSONObject decision = new JSONObject();
        decision.put("action", "fly");
        controls.addAction(decision);
        return decision;
    }
    
}