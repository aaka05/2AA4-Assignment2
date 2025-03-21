package ca.mcmaster.se2aa4.island.team31.Drone;

public class Battery {
    private final int initialBatteryLevel; // ✅ New field
    private int batteryLevel;

    public Battery(int initialBatteryLevel) {
        if (initialBatteryLevel < 0) {
            throw new IllegalArgumentException("Battery level cannot be negative");
        }
        this.initialBatteryLevel = initialBatteryLevel; // ✅ Save the original value
        this.batteryLevel = initialBatteryLevel;
    }

    public int useBattery(int amount) {
        if (amount > this.batteryLevel) {
            throw new IllegalArgumentException("Battery level cannot be negative");
        }
        this.batteryLevel -= amount;
        return this.batteryLevel;
    }

    public int getInitialBatteryLevel() {
        return initialBatteryLevel; // ✅ Now returns the true initial battery
    }

    public int getBatteryLevel() {
        return batteryLevel;
    }

    public boolean goHome(int x, int y, int costPerMove) {
        int costToGoHome = (x + y) * costPerMove;
        return batteryLevel <= costToGoHome;
    }
}
