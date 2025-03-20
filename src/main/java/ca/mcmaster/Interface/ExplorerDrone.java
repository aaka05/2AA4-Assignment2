package ca.mcmaster.Interface;

import org.json.JSONObject;


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
