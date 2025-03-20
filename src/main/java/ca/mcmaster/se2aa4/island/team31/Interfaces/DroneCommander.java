package ca.mcmaster.se2aa4.island.team31.Interfaces;

import org.json.JSONObject;
//oberserver pattern
public interface DroneCommander {
    public void addCommand(JSONObject command);

    public JSONObject getRecentCommand();
}
