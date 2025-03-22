
package ca.mcmaster.se2aa4.island.team31.Terrain;

import org.json.JSONObject;

import ca.mcmaster.se2aa4.island.team31.Drone.Sensor;
import ca.mcmaster.se2aa4.island.team31.Interfaces.Actions;

public class GoToIsland extends State {

    int distanceRemaining;

    public GoToIsland(Actions drone, Sensor sensor, int distance) {
        super(drone, sensor);
        this.distanceRemaining = distance;
    }

    @Override
    public State getNextState(JSONObject response) {
        if (this.distanceRemaining > 0) {
            drone.moveForward();
            distanceRemaining--;
            return this;
        }

        // Reached land after flying the correct number of steps
        return new OnIsland(drone, sensor);  // Or return new OnIsland(drone, sensor); if you'd like to enter a new state
    }
}
