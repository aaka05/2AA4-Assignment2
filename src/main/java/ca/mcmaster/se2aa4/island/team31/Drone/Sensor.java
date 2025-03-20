package ca.mcmaster.se2aa4.island.team31.Drone;

import ca.mcmaster.se2aa4.island.team31.Enums.Direction;
import ca.mcmaster.se2aa4.island.team31.Interface.ExplorerDrone;

public class Sensor extends ExplorerDrone {
    private Gps gps = new Gps();
    private DroneActions droneActions = new DroneActions();
    private Direction.CardinalDirection direction;

    public Sensor(Direction.CardinalDirection direction) {
        this.direction = direction;
    }

    public void echoForward(){
        update(droneActions.echo(this.direction));
    }

    public void echoRight(){
        update(droneActions.echo(gps.getRight(this.direction)));
    }

    public void echoLeft(){
        update(droneActions.echo(gps.getLeft(this.direction)));
    }

    //changes to scan different directions
    public void setHeading(Direction.CardinalDirection direction){
        this.direction = direction;
    }

    public void scan(){
        update(droneActions.scan());
    }
    
    
}
