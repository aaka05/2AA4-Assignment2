package ca.mcmaster.se2aa4.island.team31.Drone;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;

import ca.mcmaster.se2aa4.island.team31.Direction;

/**
 * Handles all possible drone actions by creating appropriate JSON commands
 */
public class DroneActions {
    private static final Logger log = LogManager.getLogger(DroneActions.class);

    //creates a command to change drone's heading
    public JSONObject heading(Direction.CardinalDirection dir) {
        JSONObject cmd = new JSONObject();
        JSONObject params = new JSONObject();
        
        cmd.put("action", "heading");
        params.put("direction", dir);
        cmd.put("parameters", params);
        return cmd;
    }

    //moves one tile forward
    public JSONObject fly() {
        JSONObject cmd = new JSONObject();
        cmd.put("action", "fly");
        return cmd;
    }

    /**
     * Sends out an echo in specified direction to detect obstacles
     * Returns distance to nearest obstacle if one exists
     */
    public JSONObject echo(Direction.CardinalDirection dir) {
        JSONObject cmd = new JSONObject();
        JSONObject params = new JSONObject();

        cmd.put("action", "echo");
        params.put("direction", dir);
        cmd.put("parameters", params);
        return cmd;
    }

    //stop and return to base
    public JSONObject stop() {
        JSONObject cmd = new JSONObject();
        cmd.put("action", "stop");
        return cmd;
    }

    //scans current tile for biomes, POIs, etc
    public JSONObject scan() {
        log.info("Initiating area scan...");
        JSONObject cmd = new JSONObject();
        cmd.put("action", "scan");
        return cmd;
    }
} 