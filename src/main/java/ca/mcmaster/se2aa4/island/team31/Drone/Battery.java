package ca.mcmaster.se2aa4.island.team31.Drone;

public class Battery {
    private int batteryLevel;

    public Battery(int initialBatteryLevel) {
        if (initialBatteryLevel < 0){
            throw new IllegalArgumentException("Battery level cannot be negative");
        }
        this.batteryLevel = initialBatteryLevel;
    }

    public int useBattery(int batteryLevel) {
        if(batteryLevel > this.batteryLevel) {
            throw new IllegalArgumentException("Battery level cannot be negative");
        }
        this.batteryLevel -= batteryLevel;
        return this.batteryLevel;
    }

    public int getInitialBatteryLevel() {
        return this.batteryLevel;
    }

    public int getBatteryLevel() {
        return batteryLevel;
    }

    public boolean goHome(int x, int y, int costPerMove){
        //cost to go home is the sum of the current x and y coordinates multiplied by the cost per move
        int costToGoHome = (x + y) * costPerMove;
        return batteryLevel <= costToGoHome;
    }
    
}
