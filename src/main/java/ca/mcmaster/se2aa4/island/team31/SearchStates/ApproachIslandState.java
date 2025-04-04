package ca.mcmaster.se2aa4.island.team31.SearchStates;

import org.json.JSONObject;

import ca.mcmaster.se2aa4.island.team31.Report;
import ca.mcmaster.se2aa4.island.team31.AbstractClasses.SearchStates;
import ca.mcmaster.se2aa4.island.team31.Drone.Sensor;
import ca.mcmaster.se2aa4.island.team31.Interfaces.Actions;

public class ApproachIslandState extends SearchStates {

    //steps we still need to take to reach the island (reaches just one tile before island)
    private int stepsRemaining;

    public ApproachIslandState(Actions drone, Sensor sensor, Report report, int distance) {
        super(drone, sensor, report);
        this.stepsRemaining = distance;
    }

    @Override
    public SearchStates getNextSearch(JSONObject response) {
        if (this.stepsRemaining > 0) {
            stepsRemaining--;
            drone.moveForward();
            return this;
        }

        //we made it to the island - switch to on island state
        return new OnIslandState(this.drone, this.sensor, this.report);
    }
}
