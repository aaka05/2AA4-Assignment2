package ca.mcmaster.se2aa4.island.team31;

import java.util.Arrays;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;

import ca.mcmaster.se2aa4.island.team31.AbstractClasses.SearchStates;
import ca.mcmaster.se2aa4.island.team31.Drone.Constraints;
import ca.mcmaster.se2aa4.island.team31.Drone.MovementController;
import ca.mcmaster.se2aa4.island.team31.Drone.Sensor;
import ca.mcmaster.se2aa4.island.team31.SearchStates.MoveDiagonalState;

/**
 * main controller class for the exploration drone
 * manages state transitions, battery levels, and exploration status
 */
public class MissionControl {

    private static final Logger log = LogManager.getLogger(MissionControl.class);

    //state and command management
    private SearchStates currentState;
    private JSONObject lastCommand;
    private boolean explorationDone;
    
    //drone components
    private final DroneActionMonitor tracker;
    private final MovementController drone;
    private final Sensor sensor;
    private final Constraints constraints;
    private final Report report;

    public MissionControl(int batteryLevel, String direction) {
        Direction.CardinalDirection startDir = Direction.CardinalDirection.valueOf(direction);
        
        //initialize all final fields directly in constructor
        this.sensor = new Sensor(startDir);
        this.drone = new MovementController(batteryLevel, startDir, this.sensor);
        this.constraints = new Constraints(this.drone);
        this.report = Report.getInstance();
        this.currentState = new MoveDiagonalState(this.drone, this.sensor, this.report);
        this.tracker = new DroneActionMonitor(Arrays.asList(this.drone, this.sensor));
        
        this.explorationDone = false;
        log.info("Drone initialized, heading {}", startDir);
    }

    //battery management
    public int getCurrentCharge() {
        return this.drone.getCurrentCharge();
    }
    
    public int getMaxCapacity() {
        return this.drone.getMaxCapacity();
    }

    private void updateBatteryLevel() {
        if (lastCommand != null && lastCommand.has("cost")) {
            int energyUsed = lastCommand.getInt("cost");
            this.drone.useBattery(energyUsed);
            log.info("Battery: {} remaining", this.drone.getCurrentCharge());
        }
    }

    //emergency stop if battery is critical (drone back to base)
    private JSONObject emergencyStop() {
        log.info("Emergency stop - battery critical");
        this.drone.stop();
        return tracker.getRecentCommand();
    }

    //main decision loop
    public JSONObject takeDecision() {
        updateBatteryLevel();
        
        if (!constraints.canContinue()) {
            explorationDone = true;
            return emergencyStop();
        }

        currentState = currentState.getNextSearch(lastCommand);
        JSONObject decision = tracker.getRecentCommand();
        
        if (isStopCommand(decision)) {
            explorationDone = true;
            log.info("Exploration complete");
        }
        
        return decision;
    }

    //checks if the command is a stop command
    private boolean isStopCommand(JSONObject decision) {
        return decision.has("action") && "stop".equals(decision.getString("action"));
    }

    //updates the last command
    public void updateDrone(JSONObject command) {
        this.lastCommand = command;
    }

    //checks if the exploration is complete
    public boolean isExplorationComplete() {
        return this.explorationDone;
    }

    //displays the POIs found
    public String getDiscoveries() {
        return this.report.displayDiscoveries();
    }

}

