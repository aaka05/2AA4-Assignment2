package ca.mcmaster.se2aa4.island.team31;

public class Battery {
    private int batteryLevel;
    //private final int initialBatteryLevel;

    public Battery(int initialBatteryLevel) {
        //this.initialBatteryLevel = initialBatteryLevel;
        this.batteryLevel = initialBatteryLevel;
    }

    public int useBattery(int batteryLevel) {
        if(batteryLevel > this.batteryLevel) {
            throw new IllegalArgumentException("Battery level cannot be negative");
        }
        this.batteryLevel -= batteryLevel;
        return this.batteryLevel;
    }

    public int getBatteryLevel() {
        return batteryLevel;
    }

    public boolean goHome(int x, int y, int costPerMove){
        int costToGoHome = (x + y) * costPerMove;
        return batteryLevel <= costToGoHome;
    }
    
}
