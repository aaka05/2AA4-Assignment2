package ca.mcmaster.se2aa4.island.team31.Terrain;
import org.json.JSONObject;


public interface Decidable {
    String takeDecision();
    void acknowledgeResults(JSONObject response);
}

