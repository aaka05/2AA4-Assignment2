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
import ca.mcmaster.se2aa4.island.team31.Terrain.Decidable;
import ca.mcmaster.se2aa4.island.team31.Terrain.FindIsland;
import ca.mcmaster.se2aa4.island.team31.Terrain.State;

public class DroneController {

    private static final Logger logger = LogManager.getLogger(DroneController.class);

    private DroneTracker droneTracker;
    private MovementController drone;
    private Sensor sensor;
    private Battery battery;
    private Constraints constraints;
    private State currentState;
    private JSONObject previousCommand;

    public DroneController(int batteryLevel, String direction) {
        Direction.CardinalDirection startDirection = Direction.CardinalDirection.E;
        try {
            startDirection = Direction.CardinalDirection.valueOf(direction);
        } catch (Exception e) {
            logger.error("Invalid direction provided, defaulting to East", e);
        }

        this.battery = new Battery(batteryLevel);
        this.drone = new MovementController(batteryLevel, direction);
        this.sensor = new Sensor(startDirection);
        this.constraints = new Constraints(this.drone);

        this.currentState = new FindIsland(this.drone, this.sensor);

        List<ExplorerDrone> subjects = Arrays.asList(this.drone, this.sensor);
        this.droneTracker = new DroneTracker(subjects);

        logger.info("DroneController initialized with direction: {}", startDirection);
    }

    public int getBatteryLevel() {
        return this.drone.getBatteryLevel();
    }

    public int getInitialBatteryLevel() {
        return this.drone.getInitialBatteryLevel();
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

        if (!constraints.enoughBattery()) {
            return stopMoving();
        }

        if (currentState instanceof Decidable decidable) {
            String decision = decidable.takeDecision();
            if (decision != null) {
                logger.info("Decision taken: {}", decision);
                this.previousCommand = new JSONObject(decision);
                return this.previousCommand;
            } else {
                logger.warn("Decidable.takeDecision() returned null.");
                return null;
            }
        }

        State nextState;
        State currentState;

        do {
            currentState = this.currentState;
            nextState = this.currentState.getNextState(previousCommand);
            this.currentState = nextState;
        } while (!currentState.equals(nextState));

        return droneTracker.getRecentCommand();
    }

    public void updateDrone(JSONObject command) {
        this.previousCommand = command;

        if (currentState instanceof Decidable decidable) {
            decidable.acknowledgeResults(command);
        }
    }
}
