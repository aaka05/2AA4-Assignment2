package ca.mcmaster.se2aa4.island.team31.Drone;

public class Battery {
    private final int maxCapacity;
    private int currentCharge;

    public Battery(int capacity) {
        if (capacity < 0) {
            throw new IllegalArgumentException("Battery capacity must be positive");
        }
        this.maxCapacity = capacity;
        this.currentCharge = capacity;
    }

    public int useBattery(int energyRequired) {
        if (energyRequired > this.currentCharge) {
            throw new IllegalArgumentException("Not enough battery charge remaining");
        }
        this.currentCharge -= energyRequired;
        return this.currentCharge;
    }

    //returns initial/max battery capacity
    public int getMaxCapacity() {
        return maxCapacity;
    }

    public int getCurrentCharge() {
        return currentCharge;
    }

    //checks if drone should return to base
    public boolean goHome(int xPos, int yPos, int energyPerMove) {
        int energyNeededForReturn = (xPos + yPos) * energyPerMove;
        return currentCharge <= energyNeededForReturn;
    }
}
