package ca.mcmaster.se2aa4.island.team31.Terrain;

import org.json.JSONObject;

import ca.mcmaster.se2aa4.island.team31.Drone.Sensor;
import ca.mcmaster.se2aa4.island.team31.Interfaces.Actions;

public class OnIsland extends State {

    public OnIsland(Actions drone, Sensor sensor) {
        super(drone, sensor);
    }

    @Override
    public State getNextState(JSONObject response) {
        // For now, do nothing — or scan, land, etc. later
        return this;
    }
}
