package ca.mcmaster.se2aa4.island.team31.Drone;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Constraints {
    private static final double LOW_BATTERY_THRESHOLD = 0.007;
    private final MovementController drone;
    private final Logger log;

    public Constraints(MovementController drone) {
        this.drone = drone;
        this.log = LogManager.getLogger();
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
