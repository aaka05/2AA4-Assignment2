package ca.mcmaster.se2aa4.island.team31.Drone;

import ca.mcmaster.se2aa4.island.team31.Direction.CardinalDirection;


 //navigation system for the drone
public class Gps {
    private static final int[][] MOVE_VECTORS = {
        {0, 1},   // NORTH
        {1, 0},   // EAST
        {0, -1},  // SOUTH
        {-1, 0}   // WEST
    };

    public CardinalDirection getRight(CardinalDirection heading) {
        //implementation of circular array
        return CardinalDirection.values()[(heading.ordinal() + 1) % 4];
    }

    public CardinalDirection getLeft(CardinalDirection heading) {
        //implementation of circular array
        return CardinalDirection.values()[(heading.ordinal() + 3) % 4];
    }

    public int[] getForwardMovement(CardinalDirection heading) {
        return MOVE_VECTORS[heading.ordinal()];
    }
}
