package ca.mcmaster.se2aa4.island.team31.Terrain;

import org.json.JSONObject;

import ca.mcmaster.se2aa4.island.team31.Drone.Sensor;
import ca.mcmaster.se2aa4.island.team31.Interfaces.Actions;

public abstract class State {
    public final Actions drone;
    public final Sensor sensor;
    //for report: 

    public State(Actions drone, Sensor sensor){
        this.drone = drone;
        this.sensor = sensor;
    }

    public abstract State getNextState(JSONObject command);
}
