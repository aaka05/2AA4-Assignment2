package ca.mcmaster.se2aa4.island.team31.Terrain;

import org.json.JSONObject;

import ca.mcmaster.se2aa4.island.team31.Drone.Sensor;
import ca.mcmaster.se2aa4.island.team31.Interfaces.Actions;

/**
 * GoToIsland.java
 *
 * This state flies forward a specified number of tiles (distanceRemaining) to
 * reach ground. Once arrived, it does a quick scan, then transitions to OnIsland.
 */
public class GoToIsland extends State {

    private int distanceRemaining;

    public GoToIsland(Actions drone, Sensor sensor, int distance) {
        super(drone, sensor);
        this.distanceRemaining = distance;
    }

    @Override
    public State getNextState(JSONObject response) {
        // Keep flying until we've covered 'distanceRemaining' steps
        if (distanceRemaining > 0) {
            drone.moveForward();
            distanceRemaining--;
            return this;  // stay in GoToIsland until done
        }

        // Once we have arrived, do a final scan to reveal the tile,
        // then switch to OnIsland for coastline logic.
        drone.scan();
        return new OnIsland(drone, sensor);
    }
}
