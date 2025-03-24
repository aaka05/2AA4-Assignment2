package ca.mcmaster.se2aa4.island.team31;

import java.util.Deque;
import java.util.LinkedList;
import java.util.List;
import java.util.Collections;
import org.json.JSONObject;

import ca.mcmaster.se2aa4.island.team31.Interfaces.DroneCommander;
import ca.mcmaster.se2aa4.island.team31.Interfaces.ExplorerDrone;

//acts as an observer for drone components to record their actions
public class DroneActionMonitor implements DroneCommander {
    //stack of commands executed by the drone
    private final Deque<JSONObject> commandHistory;
    private static final int maxHistorySize = 1000; // Prevent unbounded growth

    public DroneActionMonitor(List<ExplorerDrone> components) {
        this.commandHistory = new LinkedList<>();
        registerComponents(components);
    }

    private void registerComponents(List<ExplorerDrone> components) {
        if (components == null) {
            throw new IllegalArgumentException("Components list cannot be null");
        }
        
        components.forEach(component -> {
            if (component != null) {
                component.addObserver(this);
            }
        });
    }

    //add new command to history stack
    @Override
    public void addCommand(JSONObject command) {
        if (command == null) {
            throw new IllegalArgumentException("Command cannot be null");
        }

        if (commandHistory.size() >= maxHistorySize) {
            commandHistory.removeLast();
        }
        commandHistory.push(command);
    }

    //get most recent command without removing it
    @Override
    public JSONObject getRecentCommand() {
        return commandHistory.isEmpty() ? null : commandHistory.peek();
    }

    //new method to get all commands (immutable)
    public List<JSONObject> getAllCommands() {
        return Collections.unmodifiableList(new LinkedList<>(commandHistory));
    }

    //new method to clear history
    public void clearHistory() {
        commandHistory.clear();
    }
}
