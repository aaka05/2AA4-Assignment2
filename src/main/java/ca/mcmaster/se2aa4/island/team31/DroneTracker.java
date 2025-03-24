package ca.mcmaster.se2aa4.island.team31;

import java.util.Deque;
import java.util.LinkedList;
import java.util.List;

import org.json.JSONObject;

import ca.mcmaster.se2aa4.island.team31.Interfaces.DroneCommander;
import ca.mcmaster.se2aa4.island.team31.Interfaces.ExplorerDrone;

//acts as an observer for drone components to record their actions
public class DroneTracker implements DroneCommander {
    //stack of commands executed by the drone
    private final Deque<JSONObject> commandHistory;

    public DroneTracker(List<ExplorerDrone> components) {
        this.commandHistory = new LinkedList<>();
        
        //register as observer for all drone components
        for (ExplorerDrone component : components) {
            component.addObserver(this);
        }
    }

    //add new command to history stack
    @Override
    public void addCommand(JSONObject command) {
        commandHistory.push(command);
    }

    //get most recent command without removing it
    @Override
    public JSONObject getRecentCommand() {
        return commandHistory.peek();
    }
}
