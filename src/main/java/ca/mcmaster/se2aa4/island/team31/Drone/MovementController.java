package ca.mcmaster.se2aa4.island.team31.Drone;

import java.util.HashSet;
import java.util.Set;

import ca.mcmaster.se2aa4.island.team31.Direction;
import ca.mcmaster.se2aa4.island.team31.AbstractClasses.ExplorerDrone;
import ca.mcmaster.se2aa4.island.team31.Interfaces.Actions;

/**
 * Main controller for drone movement and navigation
 * handles position tracking, battery management, and movement commands
 */
public class MovementController extends ExplorerDrone implements Actions {
    private final Gps gps;
    private final Sensor sensor;
    private final Battery battery;
    private final DroneActions actions;
    
    //position tracking
    private int x, y;
    private Direction.CardinalDirection heading;
    private Direction.CardinalDirection searchHeading;
    
    //track visited locations and turn points for pathfinding
    private final Set<String> visitedLocations;
    private final Set<String> turnPoints;

    public MovementController(Integer batteryCapacity, Direction.CardinalDirection startDir, Sensor sensor) {
        this.gps = new Gps();
        this.battery = new Battery(batteryCapacity);
        this.heading = startDir;
        this.sensor = sensor;
        this.actions = new DroneActions();
        
        //initialize search direction (90° right of heading)
        this.searchHeading = gps.getRight(this.heading);
        
        //start at origin (0,0)
        this.x = 0;
        this.y = 0;
        
        this.visitedLocations = new HashSet<>();
        this.turnPoints = new HashSet<>();
    }

    //updates position based on current heading
    private void updatePosition() {
        int[] movement = gps.getForwardMovement(this.heading);
        this.x += movement[0];
        this.y += movement[1];
    }

    @Override
    public void moveForward() {
        updatePosition();
        update(actions.fly());
    }

    //performs a right turn (diagonal movement)
    @Override
    public void turnRight() {
        updatePosition();
        this.searchHeading = this.heading;
        this.heading = gps.getRight(this.heading);
        this.sensor.setHeading(this.heading);
        updatePosition();
        update(actions.heading(this.heading));
    }

    //performs a left turn (diagonal movement)
    @Override
    public void turnLeft() {
        updatePosition();
        this.searchHeading = this.heading;
        this.heading = gps.getLeft(this.heading);
        this.sensor.setHeading(this.heading);
        updatePosition();
        update(actions.heading(this.heading));
    }

    @Override
    public void stop() {
        update(actions.stop());
    }

    //position and heading getters
    @Override
    public int getX() { return x; }
    
    @Override
    public int getY() { return y; }
    
    @Override
    public Direction.CardinalDirection getDirection() { return heading; }
    
    @Override
    public Direction.CardinalDirection getSearchHeading() { return searchHeading; }

    //battery management
    @Override
    public int getCurrentCharge() { return battery.getCurrentCharge(); }
    
    @Override
    public void useBattery(int amount) { battery.useBattery(amount); }
    
    @Override
    public int getMaxCapacity() { return battery.getMaxCapacity(); }

    //location tracking methods
    @Override
    public boolean hasVisitedLocation() {
        String pos = x + "," + y;
        if (visitedLocations.contains(pos)) {
            return true;
        }
        visitedLocations.add(pos);
        return false;
    }

    @Override
    public boolean isTurnPoint() {
        return turnPoints.contains(x + "," + y);
    }

    @Override
    public void markAsTurnPoint() {
        turnPoints.add(x + "," + y);
    }
}