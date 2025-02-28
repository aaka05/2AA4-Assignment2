package ca.mcmaster.se2aa4.island.team31;

//i think this class should be for handling the sensor data from the drone and check for GROUND or OUT OF RANGE
public class Sensor {
    private int frontDistance;
    private int leftDistance;
    private int rightDistance;

    public Sensor(int frontDistance, int leftDistance, int rightDistance) {
        this.frontDistance = frontDistance;
        this.leftDistance = leftDistance;
        this.rightDistance = rightDistance;
     
    }
}
