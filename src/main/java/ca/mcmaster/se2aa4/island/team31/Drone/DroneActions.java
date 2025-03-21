package ca.mcmaster.se2aa4.island.team31.Drone;

import org.json.JSONObject;

import ca.mcmaster.se2aa4.island.team31.DroneController;
import ca.mcmaster.se2aa4.island.team31.Enums.Direction;
import eu.ace_design.island.game.Directions;

public class DroneActions {

    public JSONObject heading(Direction.CardinalDirection direction) {
        JSONObject decision = new JSONObject();
        JSONObject parameters = new JSONObject();
        decision.put("action", "heading");
        parameters.put("direction", direction);
        decision.put("parameters", parameters);
        return decision;
    }

    public JSONObject fly() {
        JSONObject decision = new JSONObject();
        decision.put("action", "fly");
        return decision;
    }

    public JSONObject echo(Direction.CardinalDirection direction) {
        JSONObject decision = new JSONObject();
        JSONObject parameter = new JSONObject();

        decision.put("action", "echo");
        parameter.put("direction", direction);
        decision.put("parameters", parameter);
        return decision;
    }

    public JSONObject stop() {
        JSONObject decision = new JSONObject();
        decision.put("action", "stop");
        return decision;
    }

    public JSONObject scan() {
        JSONObject decision = new JSONObject();
        decision.put("action", "scan");
        return decision;
    }
} 