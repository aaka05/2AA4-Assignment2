package ca.mcmaster.se2aa4.island.team31.Drone;

import ca.mcmaster.se2aa4.island.team31.Direction;
import ca.mcmaster.se2aa4.island.team31.Interfaces.ExplorerDrone;

/**
 * handles all sensor operations for the drone
 * includes echo detection and scanning functionality
 */
public class Sensor extends ExplorerDrone {
    private final Gps gps;
    private final DroneActions actions;
    private Direction.CardinalDirection heading;

    public Sensor(Direction.CardinalDirection startHeading) {
        this.gps = new Gps();
        this.actions = new DroneActions();
        this.heading = startHeading;
    }

    //sends echo pulse forward in current heading direction
    public void echoForward() {
        update(actions.echo(this.heading));
    }

    //sends echo pulse to the right
    public void echoRight() {
        update(actions.echo(gps.getRight(this.heading)));
    }

    //sends echo pulse to the left
    public void echoLeft() {
        update(actions.echo(gps.getLeft(this.heading)));
    }

    //updates sensor heading for direction scanning
    public void setHeading(Direction.CardinalDirection newHeading) {
        this.heading = newHeading;
    }

    //performs area scan at current location
    public void scan() {
        update(actions.scan());
    }
}
