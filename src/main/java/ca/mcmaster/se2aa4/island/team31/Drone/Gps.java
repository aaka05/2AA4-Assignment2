package ca.mcmaster.se2aa4.island.team31.Drone;

import java.util.EnumMap;
import java.util.Map;

import ca.mcmaster.se2aa4.island.team31.Direction.CardinalDirection;


 //navigation system for the drone
public class Gps {
    private final Map<CardinalDirection, CardinalDirection> rightTurns;
    private final Map<CardinalDirection, CardinalDirection> leftTurns;
    private final Map<CardinalDirection, int[]> moveVectors;

    public Gps() {
        this.rightTurns = initializeRotationMap(true);
        this.leftTurns = initializeRotationMap(false);
        this.moveVectors = initializeMoveVectors();
    }

    private Map<CardinalDirection, CardinalDirection> initializeRotationMap(boolean isRightTurn) {
        Map<CardinalDirection, CardinalDirection> rotationMap = new EnumMap<>(CardinalDirection.class);
        CardinalDirection[] directions = CardinalDirection.values();
        
        for (int i = 0; i < directions.length; i++) {
            //ternary operator to determine if we are doing a right or left turn
            int nextIndex = isRightTurn ? 
                (i + 1) % directions.length : 
                (i - 1 + directions.length) % directions.length;
            rotationMap.put(directions[i], directions[nextIndex]);
        }
        return rotationMap;
    }

    private Map<CardinalDirection, int[]> initializeMoveVectors() {
        Map<CardinalDirection, int[]> vectors = new EnumMap<>(CardinalDirection.class);
        vectors.put(CardinalDirection.N, new int[]{0, 1});   // N = +y
        vectors.put(CardinalDirection.S, new int[]{0, -1});  // S = -y
        vectors.put(CardinalDirection.E, new int[]{1, 0});   // E = +x
        vectors.put(CardinalDirection.W, new int[]{-1, 0});  // W = -x
        return vectors;
    }

    public CardinalDirection getRight(CardinalDirection heading) {
        return rightTurns.get(heading);
    }

    public CardinalDirection getLeft(CardinalDirection heading) {
        return leftTurns.get(heading);
    }

    //movement vector for the drone
    public int[] getForwardMovement(CardinalDirection heading) {
        return moveVectors.get(heading);
    }
}
