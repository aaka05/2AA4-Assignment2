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
        // Implement logic for searching the island
        return this;
    }
}
