package ca.mcmaster.se2aa4.island.team31;

import java.util.Arrays;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;

import ca.mcmaster.se2aa4.island.team31.Drone.Battery;
import ca.mcmaster.se2aa4.island.team31.Drone.Constraints;
import ca.mcmaster.se2aa4.island.team31.Drone.MovementController;
import ca.mcmaster.se2aa4.island.team31.Drone.Sensor;
import ca.mcmaster.se2aa4.island.team31.Enums.Direction;
import ca.mcmaster.se2aa4.island.team31.Interfaces.ExplorerDrone;
import ca.mcmaster.se2aa4.island.team31.Terrain.FindIsland;
import ca.mcmaster.se2aa4.island.team31.Terrain.Report;
import ca.mcmaster.se2aa4.island.team31.Terrain.State;

/**
 * main controller class for the exploration drone
 * manages state transitions, battery levels, and exploration status
 */
public class DroneController {

    private static final Logger log = LogManager.getLogger(DroneController.class);

    private final DroneTracker tracker;
    private final MovementController drone;
    private final Sensor sensor;
    private final Battery battery;
    private final Constraints constraints;
    private final Report report;

    //state management
    private State currentState;
    private JSONObject lastCommand;
    private boolean explorationDone;

    public DroneController(int batteryLevel, String direction) {
        //set initial heading (default East if invalid)
        Direction.CardinalDirection startDir = Direction.CardinalDirection.E;
        try {
            startDir = Direction.CardinalDirection.valueOf(direction);
        } catch (Exception e) {
            log.error("Bad direction input, defaulting to East", e);
        }

        //initialize drone components
        this.battery = new Battery(batteryLevel);
        this.sensor = new Sensor(startDir);
        this.drone = new MovementController(batteryLevel, startDir, this.sensor);
        this.constraints = new Constraints(this.drone);
        this.report = Report.getInstance();

        //start with island search state (zigzag pattern)
        this.currentState = new FindIsland(this.drone, this.sensor, this.report);

        //setup action tracking
        List<ExplorerDrone> components = Arrays.asList(this.drone, this.sensor);
        this.tracker = new DroneTracker(components);
        
        this.explorationDone = false;
        log.info("Drone initialized, heading " + startDir);
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

        //check battery status
        if (!constraints.canContinue()) {
            setExplorationDone(true);
            return emergencyStop();
        }

        //process state transitions
        State nextState;
        State current;
        do {
            current = this.currentState;
            nextState = this.currentState.getNextState(this.lastCommand);
            this.currentState = nextState;
        } while (!current.equals(nextState));

        JSONObject decision = tracker.getRecentCommand();
        checkForStopCommand(decision);
        return decision;
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

