package ca.mcmaster.se2aa4.island.team31.AbstractClasses;

import org.json.JSONObject;

import ca.mcmaster.se2aa4.island.team31.Interfaces.DroneCommander;


public abstract class ExplorerDrone {

    private DroneCommander droneObserver;

    public void addObserver(DroneCommander droneObserver) {
        this.droneObserver = droneObserver;
    }

    public void update(JSONObject decision) {
        if (droneObserver != null) {
            droneObserver.addCommand(decision);
        }
    }

}
