package ca.mcmaster.se2aa4.island.team31;

import java.util.Arrays;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;

import ca.mcmaster.se2aa4.island.team31.Drone.Constraints;
import ca.mcmaster.se2aa4.island.team31.Drone.MovementController;
import ca.mcmaster.se2aa4.island.team31.Drone.Sensor;
import ca.mcmaster.se2aa4.island.team31.Enums.Direction;
import ca.mcmaster.se2aa4.island.team31.Terrain.FindIsland;
import ca.mcmaster.se2aa4.island.team31.Terrain.Report;
import ca.mcmaster.se2aa4.island.team31.Terrain.State;

/**
 * main controller class for the exploration drone
 * manages state transitions, battery levels, and exploration status
 */
public class DroneController {

    private static final Logger log = LogManager.getLogger(DroneController.class);

    //state and command management
    private State currentState;
    private JSONObject lastCommand;
    private boolean explorationDone;
    
    //drone components
    private final DroneTracker tracker;
    private final MovementController drone;
    private final Sensor sensor;
    private final Constraints constraints;
    private final Report report;

    public DroneController(int batteryLevel, String direction) {
        Direction.CardinalDirection startDir = parseInitialDirection(direction);
        
        //initialize all final fields directly in constructor
        this.sensor = new Sensor(startDir);
        this.drone = new MovementController(batteryLevel, startDir, this.sensor);
        this.constraints = new Constraints(this.drone);
        this.report = Report.getInstance();
        this.currentState = new FindIsland(this.drone, this.sensor, this.report);
        this.tracker = new DroneTracker(Arrays.asList(this.drone, this.sensor));
        
        this.explorationDone = false;
        log.info("Drone initialized, heading {}", startDir);
    }

    private Direction.CardinalDirection parseInitialDirection(String direction) {
        try {
            return Direction.CardinalDirection.valueOf(direction);
        } catch (Exception e) {
            log.error("Bad direction input, defaulting to East", e);
            return Direction.CardinalDirection.E;
        }
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
            setExplorationDone(true);
            return emergencyStop();
        }

        processStateTransitions();
        
        JSONObject decision = tracker.getRecentCommand();
        checkForStopCommand(decision);
        return decision;
    }

    private void processStateTransitions() {
        State nextState;
        State current;
        do {
            current = this.currentState;
            nextState = this.currentState.getNextState(this.lastCommand);
            this.currentState = nextState;
        } while (!current.equals(nextState));
    }

    public void updateDrone(JSONObject command) {
        this.lastCommand = command;
    }

    private void checkForStopCommand(JSONObject decision) {
        if (decision.has("action") && "stop".equals(decision.getString("action"))) {
            log.info("Exploration complete");
            this.explorationDone = true;
        }
    }
    
    private void setExplorationDone(boolean done) {
        this.explorationDone = done;
    }

    public boolean isExplorationComplete() {
        return this.explorationDone;
    }

    public String getDiscoveries() {
        return this.report.displayDiscoveries();
    }

}

