package ca.mcmaster.se2aa4.island.team31;

import java.util.Deque;
import java.util.LinkedList;
import java.util.List;

import org.json.JSONObject;

import ca.mcmaster.se2aa4.island.team31.Interfaces.DroneCommander;
import ca.mcmaster.se2aa4.island.team31.Interfaces.ExplorerDrone;

public class DroneTracker implements DroneCommander {

    private Deque<JSONObject> commands = new LinkedList<>();

    public DroneTracker(List<ExplorerDrone> drones){
        for (ExplorerDrone drone : drones){
            //make subject an observer
            drone.addObserver(this);
        }
    }

    @Override
    public void addCommand(JSONObject command){
        commands.push(command);
    }

    @Override
    public JSONObject getRecentCommand(){
        return commands.peek();
    }
        
    }
