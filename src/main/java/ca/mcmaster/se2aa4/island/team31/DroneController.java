package ca.mcmaster.se2aa4.island.team31;

import java.util.LinkedList;
import java.util.Queue;

import org.json.JSONObject;

import ca.mcmaster.se2aa4.island.team31.Drone.Battery;

public class DroneController {
    private MovementController movement;
    private Battery battery;
    private Queue<JSONObject> actionQueue;

    public DroneController(int batteryLevel, int startX, int startY, String startDirection) {
        this.battery = new Battery(batteryLevel);
        this.movement = new MovementController(this, startX, startY, Direction.CardinalDirection.valueOf(startDirection));
        this.actionQueue = new LinkedList<>();
    }

    public void addAction(JSONObject decision) {
        actionQueue.add(decision);
        updateBattery(decision);
    }

    private void updateBattery(JSONObject decision) {
        String action = decision.getString("action");
        int cost = 0;

        if (action.equals("fly")) {
            cost = 1;
            movement.moveForward();
        } else if (action.equals("heading")) {
            cost = 1;
            movement.changeDirection(decision.getString("direction"));
        } else if (action.equals("scan")) {
            cost = 2;
        } else if (action.equals("echo")) {
            cost = 1;
        } else if (action.equals("stop")) {
            cost = 0;
        } else {
            throw new IllegalArgumentException("Unknown action: " + action);
        }

        battery.useBattery(cost);
    }

    public JSONObject moveForward() {
        JSONObject move = movement.moveForward();
        addAction(move);
        return move;
    }

    public JSONObject turnLeft() {
        JSONObject turn = movement.turnLeft();
        addAction(turn);
        return turn;
    }

    public JSONObject turnRight() {
        JSONObject turn = movement.turnRight();
        addAction(turn);
        return turn;
    }

    public int getBatteryLevel() { return battery.getBatteryLevel(); }
    public int getX() { return movement.getX(); }
    public int getY() { return movement.getY(); }
    public Direction.CardinalDirection getDirection() { return movement.getDirection(); }
}
