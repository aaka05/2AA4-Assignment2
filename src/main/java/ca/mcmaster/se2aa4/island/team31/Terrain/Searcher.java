package ca.mcmaster.se2aa4.island.team31.Terrain;

import org.json.JSONObject;
import ca.mcmaster.se2aa4.island.team31.Drone.Sensor;
import ca.mcmaster.se2aa4.island.team31.Interfaces.Actions;

public class Searcher extends State {

    public Searcher(Actions drone, Sensor sensor) {
        super(drone, sensor);
    }

    @Override
    public State getNextState(JSONObject response) {
        if (response.has("extras") && response.getJSONObject("extras").has("found")) {
            String foundType = response.getJSONObject("extras").getString("found");
            if (foundType.equals("GROUND")) {
                drone.stop(); // Triggers auto-return
                return this;
            }
        }
        return this;
    }
}
