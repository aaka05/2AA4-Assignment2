package ca.mcmaster.se2aa4.island.team31.Drone;
import java.util.HashSet;
import java.util.Set;

import ca.mcmaster.se2aa4.island.team31.Enums.Direction;
import ca.mcmaster.se2aa4.island.team31.Enums.Direction.CardinalDirection;
import ca.mcmaster.se2aa4.island.team31.Interfaces.Actions;
import ca.mcmaster.se2aa4.island.team31.Interfaces.ExplorerDrone;



public class MovementController extends ExplorerDrone implements Actions {

    private Gps gps = new Gps();
    private Battery battery;
    private Direction.CardinalDirection direction;
    private Direction.CardinalDirection searchHeading;
    private Set<String> turningPoints;
    private int x;
    private int y;

    private DroneActions droneActions = new DroneActions();
    
    public MovementController(Integer batteryAmount, String startPosition) {
        this.battery = new Battery(batteryAmount);
        this.searchHeading = gps.getRight(direction);
        this.turningPoints = new HashSet<>();

        x = 0;
        y= 0;
        //Integer.parseInt(startPosition.split(",")[1]);
        //Integer.parseInt(startPosition.split(",")[0]);

        try{
            this.direction = CardinalDirection.valueOf(startPosition);
            } catch (Exception e) {
                e.printStackTrace(System.err);
            }

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
        this.direction = gps.getRight(this.direction);

        movement();
        update(droneActions.heading(this.direction));
    }

    @Override
    public void turnLeft() {
        //moves diagonal to the left 
        movement();
        this.direction = gps.getLeft(this.direction);
        movement();
        update(droneActions.heading(this.direction));
    }

    @Override
    public void stop() {
        update(droneActions.stop());
    }

    public void comeHome() {
        //logic to go home
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
    public CardinalDirection getDirection() {
        return direction;
    }

    @Override
    public int getInitialBatteryLevel() {
        return this.battery.getInitialBatteryLevel();
    }

    @Override
    public Direction.CardinalDirection getSearchHeading() {
        return this.searchHeading;
    }
    
}