package ca.mcmaster.se2aa4.island.team31;

public class Battery {
    private int batteryLevel;

    public Battery(int batteryLevel) {
        if (batteryLevel < 0 || batteryLevel > 100) {
            //battery level should be valid between 0 and 100
            throw new IllegalArgumentException("Battery level must be between 0 and 100");
        }
        this.batteryLevel = batteryLevel;
    }

    public int getBatteryLevel() {
        return batteryLevel;
    }
    
    
}
