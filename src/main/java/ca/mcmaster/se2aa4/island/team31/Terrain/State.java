package ca.mcmaster.se2aa4.island.team31.Terrain;

import org.json.JSONObject;

import ca.mcmaster.se2aa4.island.team31.Drone.Sensor;
import ca.mcmaster.se2aa4.island.team31.Interfaces.Actions;

public abstract class State {
    protected final Actions drone;
    protected final Sensor sensor;

    public State(Actions drone, Sensor sensor) {
        this.drone = drone;
        this.sensor = sensor;
    }

    public abstract State getNextState(JSONObject command);
}
