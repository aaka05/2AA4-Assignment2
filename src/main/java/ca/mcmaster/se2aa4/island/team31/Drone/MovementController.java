package ca.mcmaster.se2aa4.island.team31.Drone;
import java.util.HashSet;
import java.util.Set;

import ca.mcmaster.se2aa4.island.team31.Enums.Direction;
import ca.mcmaster.se2aa4.island.team31.Interfaces.Actions;
import ca.mcmaster.se2aa4.island.team31.Interfaces.ExplorerDrone;


public class MovementController extends ExplorerDrone implements Actions {

    private Gps gps = new Gps();
    private Sensor sensor;
    private Battery battery;
    private Direction.CardinalDirection direction;
    private Direction.CardinalDirection searchHeading;
    private Set<String> turningPoints;
    private int x;
    private int y;

    private int costPerAction;
    private Set<String> visitedLocations;
    private Set<String> turnPoints;

    private DroneActions droneActions = new DroneActions();
    
    public MovementController(Integer batteryAmount, Direction.CardinalDirection startPosition, Sensor sensor) {
        this.battery = new Battery(batteryAmount);
        this.direction = startPosition;
        this.sensor = sensor;
        this.searchHeading = gps.getRight(this.direction);
        this.turnPoints = new HashSet<>();
        this.visitedLocations = new HashSet<>();
        x = 0;
        y= 0;
        //Integer.parseInt(startPosition.split(",")[1]);
        //Integer.parseInt(startPosition.split(",")[0]);

    }

    private void movement() {
        int[] movement = gps.getForwardMovement(this.direction);
        this.x += movement[0];
        this.y += movement[1];
    }


    @Override
    public void moveForward() {
        movement();
        update(droneActions.fly());
    }

    @Override
    public void turnRight() {
        //moves diagonal to the right
        movement();
        this.searchHeading= this.direction;
        this.direction = gps.getRight(this.direction);
        this.sensor.setHeading(this.direction);

        movement();

        update(droneActions.heading(this.direction));
    }

    @Override
    public void turnLeft() {
        //moves diagonal to the left 
        movement();
        this.searchHeading= this.direction;
        this.direction = gps.getLeft(this.direction);
        this.sensor.setHeading(this.direction);
        movement();
        update(droneActions.heading(this.direction));
    }

    @Override
    public void stop() {
        update(droneActions.stop());
    }

    @Override
    public int getBatteryLevel() {
        return battery.getBatteryLevel();
    }

    @Override
    public void useBattery(int batteryLevel) {
        battery.useBattery(batteryLevel);
    }

    @Override
    public int getX() {
        return x;
    }

    @Override
    public int getY() {
        return y;
    }

    @Override
    public Direction.CardinalDirection getDirection() {
        return this.direction;
    }

    @Override
    public int getInitialBatteryLevel() {
        return this.battery.getInitialBatteryLevel();
    }

    @Override
    public Direction.CardinalDirection getSearchHeading() {
        return this.searchHeading;
    }


    @Override
    public boolean hasVisitedLocation() {
        String positionKey = x + "," + y; // unique key for (x, y)
        if (visitedLocations.contains(positionKey)) {
            return true; // already visited this location
        }
        visitedLocations.add(positionKey); // add new location to the set
        return false;
    }

    @Override
    public boolean isTurnPoint() {
        String positionKey = x + "," + y;
        if (turnPoints.contains(positionKey)) {
            return true; // current location is known to be a point to make a turn
        }
        return false;
    }

    @Override
    public void markAsTurnPoint() {
        String positionKey = x + "," + y;
        turnPoints.add(positionKey);
    }

    
}