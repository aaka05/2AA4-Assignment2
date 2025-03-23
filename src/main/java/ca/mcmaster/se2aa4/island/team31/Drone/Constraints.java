package ca.mcmaster.se2aa4.island.team31.Drone;

import java.util.EnumMap;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import ca.mcmaster.se2aa4.island.team31.Enums.Direction.CardinalDirection;

public class Constraints {
    private static final double LOW_BATTERY_THRESHOLD = 0.007;
    private final MovementController drone;
    private final Logger log;
    private final Map<CardinalDirection, CardinalDirection> illegalTurns;

    public Constraints(MovementController drone) {
        this.drone = drone;
        this.log = LogManager.getLogger();
        
        //map out illegal 180° turns
        illegalTurns = new EnumMap<>(CardinalDirection.class);
        illegalTurns.put(CardinalDirection.N, CardinalDirection.S);
        illegalTurns.put(CardinalDirection.S, CardinalDirection.N);
        illegalTurns.put(CardinalDirection.E, CardinalDirection.W);
        illegalTurns.put(CardinalDirection.W, CardinalDirection.E);
    }

    public boolean isBadTurn(CardinalDirection current, CardinalDirection next) {
        return illegalTurns.get(current) == next;
    }

    //checks if drone needs to return to base
    public boolean needsRecharge() {
        if (drone.getCurrentCharge() < (drone.getMaxCapacity() * LOW_BATTERY_THRESHOLD)) {
            log.warn("Low battery - returning to charging station");
            return true;
        }
        return false;
    }

    //checks if drone can continue exploring
    public boolean canContinue() {
        return !needsRecharge() && 
               drone.getCurrentCharge() >= (drone.getMaxCapacity() * LOW_BATTERY_THRESHOLD);
    }
}
