package ca.mcmaster.se2aa4.island.team31.Terrain;

import org.json.JSONObject;

import ca.mcmaster.se2aa4.island.team31.Drone.Sensor;
import ca.mcmaster.se2aa4.island.team31.Interfaces.Actions;

public abstract class State {
    protected final Actions drone;
    protected final Sensor sensor;
    protected final Report report;

    public State(Actions drone, Sensor sensor, Report report) {
        this.drone = drone;
        this.sensor = sensor;
        this.report = report;
    }

    public abstract State getNextState(JSONObject command);
}
