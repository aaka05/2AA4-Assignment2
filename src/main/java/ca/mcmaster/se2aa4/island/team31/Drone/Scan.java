package ca.mcmaster.se2aa4.island.team31.Drone;

import org.json.JSONObject;

import ca.mcmaster.se2aa4.island.team31.DroneController;
import ca.mcmaster.se2aa4.island.team31.Interfaces.DroneAction;

public class Scan implements DroneAction {
    private DroneController controls;

    public Scan(DroneController controls) {
        this.controls = controls;
    }

    public JSONObject action() {
        JSONObject decision = new JSONObject();
        
        decision.put("action", "scan");
        controls.addAction(decision);
        return decision;
    }
} 