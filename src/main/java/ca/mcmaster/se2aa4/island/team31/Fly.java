package ca.mcmaster.se2aa4.island.team31;

import org.json.JSONObject;
import ca.mcmaster.se2aa4.island.team31.Interfaces.DroneAction;

public class Fly implements DroneAction {
    public Fly(DroneController controls) {
    }

    @Override
    public void action() {
        JSONObject decision = new JSONObject();
        decision.put("action", "fly");
    }
}