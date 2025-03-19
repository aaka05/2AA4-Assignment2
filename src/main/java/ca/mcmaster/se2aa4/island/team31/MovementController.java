package ca.mcmaster.se2aa4.island.team31;

import org.json.JSONObject;

import ca.mcmaster.se2aa4.island.team31.Direction.CardinalDirection;
import ca.mcmaster.se2aa4.island.team31.Drone.Fly;
import ca.mcmaster.se2aa4.island.team31.Drone.Heading;

public class MovementController {
    private int x, y;
    private CardinalDirection direction;
    private DroneController droneController;

    public MovementController(DroneController droneController, int startX, int startY, CardinalDirection startDirection) {
        this.droneController = droneController;
        this.x = startX;
        this.y = startY;
        this.direction = startDirection;
    }

    // Moves forward and updates position
    public JSONObject moveForward() {
        JSONObject move = new Fly(droneController).action();
        updatePosition();
        return move;
    }

    // Changes direction based on input
    public JSONObject changeDirection(String newDirection) {
        CardinalDirection newDir = CardinalDirection.valueOf(newDirection);
        JSONObject turn = new Heading(droneController, newDir).action();
        this.direction = newDir;
        return turn;
    }

    // Turns left and updates direction
    public JSONObject turnLeft() {
        CardinalDirection newDirection = leftDirection();
        return changeDirection(newDirection.toString());
    }

    // Turns right and updates direction
    public JSONObject turnRight() {
        CardinalDirection newDirection = rightDirection();
        return changeDirection(newDirection.toString());
    }

    // Updates position based on direction
    private void updatePosition() {
        if (direction == CardinalDirection.N) {
            y += 1;
        } else if (direction == CardinalDirection.S) {
            y -= 1;
        } else if (direction == CardinalDirection.E) {
            x += 1;
        } else if (direction == CardinalDirection.W) {
            x -= 1;
        }
    }

    // Determines left turn direction
    private CardinalDirection leftDirection() {
        if (direction == CardinalDirection.N) return CardinalDirection.W;
        if (direction == CardinalDirection.W) return CardinalDirection.S;
        if (direction == CardinalDirection.S) return CardinalDirection.E;
        return CardinalDirection.N;
    }

    // Determines right turn direction
    private CardinalDirection rightDirection() {
        if (direction == CardinalDirection.N) return CardinalDirection.E;
        if (direction == CardinalDirection.E) return CardinalDirection.S;
        if (direction == CardinalDirection.S) return CardinalDirection.W;
        return CardinalDirection.N;
    }

    // Getters
    public int getX() { return x; }
    public int getY() { return y; }
    public CardinalDirection getDirection() { return direction; }
}
