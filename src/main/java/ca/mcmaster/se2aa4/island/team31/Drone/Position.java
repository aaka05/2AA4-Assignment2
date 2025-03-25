package ca.mcmaster.se2aa4.island.team31.Drone;

/**
 * Represents a 2D position with x and y coordinates
 */
public class Position {
    private int x;
    private int y;

    public Position() {
        this(1, 1);
    }

    public Position(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void move(int x, int y) {
        this.x += x;
        this.y += y;
    }

    public int getX() { 
        return x; 
    }

    public int getY() { 
        return y; 
    }

    //returns the position as a string
    @Override
    public String toString() {
        return new StringBuilder()
            .append(x)
            .append(",")
            .append(y)
            .toString();
    }
} 