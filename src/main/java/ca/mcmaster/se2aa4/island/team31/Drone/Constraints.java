package ca.mcmaster.se2aa4.island.team31.Drone;

import java.util.EnumMap;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import ca.mcmaster.se2aa4.island.team31.Enums.Direction.CardinalDirection;

public class Constraints {

    private final MovementController droneController;
    private final Logger logger = LogManager.getLogger();
    private final Map<CardinalDirection, CardinalDirection> restrictedTurns;

    public Constraints(MovementController droneController) {
        this.droneController = droneController;

        // Define invalid 180-degree turns
        this.restrictedTurns = new EnumMap<>(CardinalDirection.class);
        this.restrictedTurns.put(CardinalDirection.N, CardinalDirection.S);
        this.restrictedTurns.put(CardinalDirection.S, CardinalDirection.N);
        this.restrictedTurns.put(CardinalDirection.E, CardinalDirection.W);
        this.restrictedTurns.put(CardinalDirection.W, CardinalDirection.E);
    }

    // Check if the move is an invalid U-turn
    public boolean isInvalidUTurn(CardinalDirection currentHeading, CardinalDirection newHeading) {
        return restrictedTurns.get(currentHeading) == newHeading;
    }

    // Check if the drone should return home due to low battery
    public boolean shouldReturnHome() {
        int currentBattery = droneController.getBatteryLevel();
        int initialBattery = droneController.getInitialBatteryLevel();

        if (currentBattery < (initialBattery * 0.5)) {
            logger.warn("Battery critically low! Returning to base.");
            return true;
        }
        return false;
    }

    // Optional: Check if there’s enough battery to continue
    public boolean enoughBattery() {
        return droneController.getBatteryLevel() >= (droneController.getInitialBatteryLevel() * 0.007);
    }
}
