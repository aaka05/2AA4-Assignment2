package ca.mcmaster.se2aa4.island.team31;

public class Sensor {
    private int frontDistance;
    private int leftDistance;
    private int rightDistance;
    private String frontStatus;
    private String leftStatus;
    private String rightStatus;

    public Sensor() {
        // Default values: assume no land detected at start
        this.frontDistance = -1;
        this.leftDistance = -1;
        this.rightDistance = -1;
        this.frontStatus = "UNKNOWN";
        this.leftStatus = "UNKNOWN";
        this.rightStatus = "UNKNOWN";
    }

    public void updateFront(int distance, String status) {  // this gets the current params from Echo.java and stores them so u can check later whats around u in the navigation code
        this.frontDistance = distance;
        this.frontStatus = status;
    }

    public void updateLeft(int distance, String status) {
        this.leftDistance = distance;
        this.leftStatus = status;
    }

    public void updateRight(int distance, String status) {
        this.rightDistance = distance;
        this.rightStatus = status;
    }

    public boolean isLandAhead() {
        return "GROUND".equals(frontStatus);
    }

    public boolean isLandLeft() {
        return "GROUND".equals(leftStatus);
    }

    public boolean isLandRight() {
        return "GROUND".equals(rightStatus);
    }

    public boolean isOutOfRangeAhead() {
        return "OUT_OF_RANGE".equals(frontStatus);
    }

    public boolean isOutOfRangeLeft() {
        return "OUT_OF_RANGE".equals(leftStatus);
    }

    public boolean isOutOfRangeRight() {
        return "OUT_OF_RANGE".equals(rightStatus);
    }

    public int getFrontDistance() {
        return frontDistance;
    }

    public int getLeftDistance() {
        return leftDistance;
    }

    public int getRightDistance() {
        return rightDistance;
    }

    public String getFrontStatus() {
        return frontStatus;
    }

    public String getLeftStatus() {
        return leftStatus;
    }

    public String getRightStatus() {
        return rightStatus;
    }
}
