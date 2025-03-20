package ca.mcmaster.se2aa4.island.team31.Interface;

import org.json.JSONObject;

public interface DroneCommander {
    public void addCommand(JSONObject command);

    public JSONObject getRecentCommand();
}
