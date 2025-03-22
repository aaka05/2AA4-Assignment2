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

        if (currentBattery < (initialBattery * 0.25)) {
            logger.warn("Battery critically low! Returning to base.");
            return true;
        }
        return false;
    }

    // Check if there's enough battery to continue
    public boolean enoughBattery() {
        // If we should return home, we don't have enough battery to continue exploring
        if (shouldReturnHome()) {
            return false;
        }
        // Keep the minimum battery check as an additional safeguard
        return droneController.getBatteryLevel() >= (droneController.getInitialBatteryLevel() * 0.007);
    }
}
