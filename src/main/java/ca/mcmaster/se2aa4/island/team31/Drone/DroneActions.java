package ca.mcmaster.se2aa4.island.team31.Drone;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;

import ca.mcmaster.se2aa4.island.team31.Enums.Direction;

public class DroneActions {
    private static final Logger logger = LogManager.getLogger(DroneActions.class);

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
        logger.info("Scanning from drone actions");
        JSONObject decision = new JSONObject();
        decision.put("action", "scan");
        return decision;
    }
} 