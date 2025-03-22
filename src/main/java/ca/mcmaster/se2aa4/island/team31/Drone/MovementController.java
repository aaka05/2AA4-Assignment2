package ca.mcmaster.se2aa4.island.team31.Drone;

import ca.mcmaster.se2aa4.island.team31.Enums.Direction;
import ca.mcmaster.se2aa4.island.team31.Enums.Direction.CardinalDirection;
import ca.mcmaster.se2aa4.island.team31.Interfaces.Actions;
import ca.mcmaster.se2aa4.island.team31.Interfaces.ExplorerDrone;
import org.json.JSONObject;

public class MovementController extends ExplorerDrone implements Actions {

    private Gps gps = new Gps();
    private Battery battery;
    private CardinalDirection direction;
    private int x;
    private int y;
    private int costPerAction;

    private DroneActions droneActions = new DroneActions();

    public MovementController(Integer batteryAmount, String startPosition) {
        this.battery = new Battery(batteryAmount);
        this.costPerAction = costPerAction;

        x = 0;
        y = 0;

        try {
            this.direction = CardinalDirection.valueOf(startPosition);
        } catch (Exception e) {
            e.printStackTrace(System.err);
            this.direction = CardinalDirection.E;
        }
    }

    @Override
    public void moveForward() {
        int[] movement = gps.getForwardMovement(this.direction);
        x += movement[0];
        y += movement[1];
        update(droneActions.fly());
    }

    @Override
    public void turnRight() {
        CardinalDirection newDir = gps.getRight(this.direction);
        if (newDir != this.direction) {
            this.direction = newDir;
            update(droneActions.heading(newDir));
        }
    }

    @Override
    public void turnLeft() {
        CardinalDirection newDir = gps.getLeft(this.direction);
        if (newDir != this.direction) {
            this.direction = newDir;
            update(droneActions.heading(newDir));
        }
    }

    @Override
    public void stop() {
        update(droneActions.stop());
    }

    @Override
    public JSONObject scan() {
        JSONObject scanCmd = droneActions.scan();
        update(scanCmd);
        return scanCmd;
    }

    @Override
    public JSONObject echo(CardinalDirection dir) {
        JSONObject echoCmd = droneActions.echo(dir);
        update(echoCmd);
        return echoCmd;
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
    public int getInitialBatteryLevel() {
        return battery.getInitialBatteryLevel();
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
}