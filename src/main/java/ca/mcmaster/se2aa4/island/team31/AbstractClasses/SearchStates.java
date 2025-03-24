package ca.mcmaster.se2aa4.island.team31.AbstractClasses;

import org.json.JSONObject;

import ca.mcmaster.se2aa4.island.team31.Report;
import ca.mcmaster.se2aa4.island.team31.Drone.Sensor;
import ca.mcmaster.se2aa4.island.team31.Interfaces.Actions;

/**
 * States can include:
 * - Finding the island
 * - Exploring the island
 * - Re-finding the island
 * - Making turns
 */
public abstract class SearchStates {
    protected final Actions drone;
    protected final Sensor sensor;
    protected final Report report;

    public SearchStates(Actions drone, Sensor sensor, Report report) {
        this.drone = drone;
        this.sensor = sensor;
        this.report = report;
    }

    public abstract SearchStates getNextSearch(JSONObject command);
}
