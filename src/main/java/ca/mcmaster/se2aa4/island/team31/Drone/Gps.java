package ca.mcmaster.se2aa4.island.team31.Drone;

import java.util.EnumMap;
import java.util.Map;
import ca.mcmaster.se2aa4.island.team31.Enums.Direction.CardinalDirection;

// GPS system for handling the drone's directional movement
public class Gps {

    private final Map<CardinalDirection, CardinalDirection> rightTurns;
    private final Map<CardinalDirection, CardinalDirection> leftTurns;
    private final Map<CardinalDirection, int[]> movements;

    public Gps() {
        // Initialize directional mappings using EnumMap for efficiency
        this.rightTurns = new EnumMap<>(CardinalDirection.class);
        this.rightTurns.put(CardinalDirection.N, CardinalDirection.E);
        this.rightTurns.put(CardinalDirection.E, CardinalDirection.S);
        this.rightTurns.put(CardinalDirection.S, CardinalDirection.W);
        this.rightTurns.put(CardinalDirection.W, CardinalDirection.N);

        this.leftTurns = new EnumMap<>(CardinalDirection.class);
        this.leftTurns.put(CardinalDirection.N, CardinalDirection.W);
        this.leftTurns.put(CardinalDirection.W, CardinalDirection.S);
        this.leftTurns.put(CardinalDirection.S, CardinalDirection.E);
        this.leftTurns.put(CardinalDirection.E, CardinalDirection.N);

        this.movements = new EnumMap<>(CardinalDirection.class);
        this.movements.put(CardinalDirection.N, new int[]{0, 1});  // Move up
        this.movements.put(CardinalDirection.S, new int[]{0, -1}); // Move down
        this.movements.put(CardinalDirection.E, new int[]{1, 0});  // Move right
        this.movements.put(CardinalDirection.W, new int[]{-1, 0}); // Move left
    }

    // Get the direction when turning right
    public CardinalDirection getRight(CardinalDirection currHeading) {
        return rightTurns.get(currHeading);
    }

    // Get the direction when turning left
    public CardinalDirection getLeft(CardinalDirection currHeading) {
        return leftTurns.get(currHeading);
    }

    // Get the movement when moving forward
    public int[] getForwardMovement(CardinalDirection currHeading) {
        return movements.get(currHeading);
    }
}
