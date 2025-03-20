package ca.mcmaster.se2aa4.island.team31.Drone;


public class Constraints {
    private MovementController drone;

    public Constraints(MovementController drone) {
        this.drone = drone;
        
    }

    public boolean enoughBattery() {
        return this.drone.getBatteryLevel() > (this.drone.getInitialBatteryLevel() * 0.01);
    }

}
