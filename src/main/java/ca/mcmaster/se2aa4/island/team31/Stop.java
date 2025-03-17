package ca.mcmaster.se2aa4.island.team31;
import org.json.JSONObject;

import ca.mcmaster.se2aa4.island.team31.Interfaces.DroneActions;

public class Stop implements DroneActions {


    @Override
    public void action() {
        JSONObject decision = new JSONObject();
        decision.put("action", "stop");

        System.out.println(decision.toString()); // send this to the drone
    }
}
