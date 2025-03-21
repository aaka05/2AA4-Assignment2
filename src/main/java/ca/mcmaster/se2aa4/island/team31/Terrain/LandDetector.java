package ca.mcmaster.se2aa4.island.team31.Terrain;

import org.json.JSONObject;

public class LandDetector {

    //this function checks if we hit the ground or not
    public boolean foundGround(JSONObject response) {
        if (response == null || !response.has("extras")) {
            return false;
        }
        JSONObject extras = response.getJSONObject("extras");
        if (extras.has("found")) {
            String found = extras.getString("found");
            return "GROUND".equals(found);  // checks if "found" is "GROUND"
        }
        return false;
    }

    //gives back how far away we are from the ground
    public int getDistance(JSONObject response) {
        int range = 0;
        if (response != null && response.has("extras")) {
            JSONObject extras = response.getJSONObject("extras");
            if (extras.has("range")) {
                range = extras.getInt("range");
            }
        }
        return range;
    }
}
