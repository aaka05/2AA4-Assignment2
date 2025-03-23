package ca.mcmaster.se2aa4.island.team31.Drone;

import java.util.EnumMap;
import java.util.Map;
import ca.mcmaster.se2aa4.island.team31.Enums.Direction.CardinalDirection;

/**
 * Navigation system for the drone
 * Handles all directional calculations and movement vectors
 */
public class Gps {
    //maps for calculating new directions after turns
    private final Map<CardinalDirection, CardinalDirection> rightTurns;
    private final Map<CardinalDirection, CardinalDirection> leftTurns;
    
    //movement vectors for each direction [x, y]
    private final Map<CardinalDirection, int[]> moveVectors;

    public Gps() {
        //right turns 
        this.rightTurns = new EnumMap<>(CardinalDirection.class);
        this.rightTurns.put(CardinalDirection.N, CardinalDirection.E);
        this.rightTurns.put(CardinalDirection.E, CardinalDirection.S);
        this.rightTurns.put(CardinalDirection.S, CardinalDirection.W);
        this.rightTurns.put(CardinalDirection.W, CardinalDirection.N);

        //left turns 
        this.leftTurns = new EnumMap<>(CardinalDirection.class);
        this.leftTurns.put(CardinalDirection.N, CardinalDirection.W);
        this.leftTurns.put(CardinalDirection.W, CardinalDirection.S);
        this.leftTurns.put(CardinalDirection.S, CardinalDirection.E);
        this.leftTurns.put(CardinalDirection.E, CardinalDirection.N);

        //define movement vectors for each direction
        this.moveVectors = new EnumMap<>(CardinalDirection.class);
        this.moveVectors.put(CardinalDirection.N, new int[]{0, 1});   // North = +y
        this.moveVectors.put(CardinalDirection.S, new int[]{0, -1});  // South = -y
        this.moveVectors.put(CardinalDirection.E, new int[]{1, 0});   // East = +x
        this.moveVectors.put(CardinalDirection.W, new int[]{-1, 0});  // West = -x
    }

    public CardinalDirection getRight(CardinalDirection heading) {
        return rightTurns.get(heading);
    }

    public CardinalDirection getLeft(CardinalDirection heading) {
        return leftTurns.get(heading);
    }

    // Returns movement vector [dx, dy] for given direction
    public int[] getForwardMovement(CardinalDirection heading) {
        return moveVectors.get(heading);
    }
}
