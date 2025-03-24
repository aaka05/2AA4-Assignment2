package ca.mcmaster.se2aa4.island.team31.Interfaces;

import org.json.JSONObject;

public interface ILandDetector {
    boolean foundGround(JSONObject response);
    int getDistance(JSONObject response);
}
