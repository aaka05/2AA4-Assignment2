package ca.mcmaster.se2aa4.island.team31.Drone;

import ca.mcmaster.se2aa4.island.team31.Enums.Direction.CardinalDirection;


//get the direction of the drone
public class Gps {

    //Circular array of directions
    private static final CardinalDirection[] RIGHT_TURNS = {CardinalDirection.N, CardinalDirection.E, CardinalDirection.S, CardinalDirection.W};
    private static final CardinalDirection[] LEFT_TURNS = {CardinalDirection.N, CardinalDirection.W, CardinalDirection.S, CardinalDirection.E};
    private static final int[][] MOVEMENTS = {
        {0, 1},   // North -> move up
        {0, -1},  // South -> move down
        {1, 0},   // East -> move right
        {-1, 0}   // West -> move left
    };

    public Gps() {
        // Constructor remains empty as we're using arrays
    }

    // Get the direction when turning right
    public CardinalDirection getRight(CardinalDirection currHeading) {
        int index = getDirectionIndex(currHeading);
        return RIGHT_TURNS[(index + 1) % RIGHT_TURNS.length];
    }

    // Get the direction when turning left
    public CardinalDirection getLeft(CardinalDirection currHeading) {
        int index = getDirectionIndex(currHeading);
        return LEFT_TURNS[(index + 1) % LEFT_TURNS.length];
    }

    // Get the movement when moving forward
    public int[] getForwardMovement(CardinalDirection currHeading) {
        int index = getDirectionIndex(currHeading);
        return MOVEMENTS[index];
    }

    // Helper method to get the index of the direction
    private int getDirectionIndex(CardinalDirection currHeading) {
        for (int i = 0; i < RIGHT_TURNS.length; i++) {
            if (RIGHT_TURNS[i] == currHeading) {
                return i;
            }
        }
        throw new IllegalArgumentException("Unknown direction: " + currHeading);
    }
}


    
