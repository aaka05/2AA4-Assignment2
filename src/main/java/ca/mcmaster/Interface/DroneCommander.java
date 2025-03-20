package ca.mcmaster.Interface;

import org.json.JSONObject;

public interface DroneCommander {
    public void addCommand(JSONObject command);

    public JSONObject getRecentCommand();
}
