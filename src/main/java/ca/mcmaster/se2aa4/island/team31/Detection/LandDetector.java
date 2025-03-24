package ca.mcmaster.se2aa4.island.team31.Detection;

import org.json.JSONObject;

public class LandDetector {

    // Helper method to safely get the extras object
    private JSONObject getExtras(JSONObject response) {
        if (response != null && response.has("extras")) {
            return response.getJSONObject("extras");
        }
        return null;
    }

    //checks if the drone's sensor detected ground
    public boolean foundGround(JSONObject response) {
        JSONObject extras = getExtras(response);
        if (extras != null && extras.has("found")) {
            return "GROUND".equals(extras.getString("found"));
        }
        return false;
    }

    //returns how many steps away the ground is
    public int getDistance(JSONObject response) {
        JSONObject extras = getExtras(response);
        if (extras != null && extras.has("range")) {
            return extras.getInt("range");
        }
        return 0;
    }
}
