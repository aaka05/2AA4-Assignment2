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
import ca.mcmaster.se2aa4.island.team31.Terrain.State;
import ca.mcmaster.se2aa4.island.team31.Terrain.Report;


public class DroneController {

    private static final Logger logger = LogManager.getLogger(DroneController.class);

    private DroneTracker droneTracker;
    private MovementController drone;
    private Sensor sensor;
    private Report report;
    private Battery battery;
    private Constraints constraints;
    private State currentState;
    private JSONObject previousCommand;
    private boolean isExplorationComplete;


    public DroneController(int batteryLevel, String direction) {
        // Set initial direction, defaulting to East if invalid
        Direction.CardinalDirection startDirection = Direction.CardinalDirection.E;
        try {
            startDirection = Direction.CardinalDirection.valueOf(direction);
        } catch (Exception e) {
            logger.error("Invalid direction provided, defaulting to East", e);
        }

        // Initialize components
        this.battery = new Battery(batteryLevel);
        this.sensor = new Sensor(startDirection);
        this.drone = new MovementController(batteryLevel, startDirection,this.sensor);
        this.constraints = new Constraints(this.drone);
        this.report = Report.getInstance();


        // Start in the FindIsland state
        this.currentState = new FindIsland(this.drone, this.sensor, this.report);

        // Track drone actions
        List<ExplorerDrone> subjects = Arrays.asList(this.drone, this.sensor);
        this.droneTracker = new DroneTracker(subjects);

        isExplorationComplete = false;

        logger.info("DroneController initialized with direction: {}", startDirection);
    }
    public int getBatteryLevel() {
        return this.drone.getBatteryLevel();  // Ensure MovementController has this method
    }
    
    public int getInitialBatteryLevel() {
        return this.drone.getInitialBatteryLevel();  // Ensure MovementController has this method
    }

    private void updateBatteryLevel() {
        if (previousCommand != null && previousCommand.has("cost")) {
            int batteryUsed = previousCommand.getInt("cost");
            this.drone.useBattery(batteryUsed);
            logger.info("Battery updated: {} remaining", this.drone.getBatteryLevel());
        }
    }

    private JSONObject stopMoving() {
        logger.info("Stopping drone due to low battery.");
        this.drone.stop();
        return droneTracker.getRecentCommand();
    }

    public JSONObject takeDecision() {
        updateBatteryLevel();

        // Stop if battery is insufficient
        if (!constraints.enoughBattery()) {
            setExplorationIsComplete(true);
            return stopMoving();
        }

        State nextState;
        State currentState;

        // Keep transitioning states until no change occurs
        do {
            currentState = this.currentState; // Store current state
            nextState = this.currentState.getNextState(this.previousCommand);
            this.currentState = nextState; // Update to next state
        } while (!currentState.equals(nextState));


        JSONObject decision = droneTracker.getRecentCommand();
        // checks if decision is to stop
        setExplorationIsComplete(decision);

        return decision;
    }

    public void updateDrone(JSONObject command) {
        this.previousCommand = command;
    }


    private void setExplorationIsComplete(JSONObject decision) {
        if (decision.has("action") && "stop".equals(decision.getString("action"))) {
            // The decision was to stop
            logger.info("** Stopping exploration");
            this.isExplorationComplete = true;
        }
    }

    
    private void setExplorationIsComplete(boolean decisionIsToStop) {
        this.isExplorationComplete = decisionIsToStop;
    }

    public boolean isExplorationComplete() {
        return this.isExplorationComplete;
    }


    public String getDiscoveries() {
        return this.report.presentDiscoveries();
    }

}

