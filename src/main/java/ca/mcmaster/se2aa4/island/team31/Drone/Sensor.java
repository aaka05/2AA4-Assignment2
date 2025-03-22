package ca.mcmaster.se2aa4.island.team31.Drone;

import ca.mcmaster.se2aa4.island.team31.Enums.Direction;
import ca.mcmaster.se2aa4.island.team31.Enums.Direction.CardinalDirection;
import ca.mcmaster.se2aa4.island.team31.Interfaces.ExplorerDrone;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Sensor extends ExplorerDrone {
    private Gps gps = new Gps();
    private DroneActions droneActions = new DroneActions();
    private CardinalDirection direction;

    private Map<String, int[]> creeks = new HashMap<>();
    private Map<String, int[]> sites = new HashMap<>();

    public Sensor(CardinalDirection direction) {
        this.direction = direction;
    }

    public void echoForward() {
        update(droneActions.echo(this.direction));
    }

    public void echoRight() {
        update(droneActions.echo(gps.getRight(this.direction)));
    }

    public void echoLeft() {
        update(droneActions.echo(gps.getLeft(this.direction)));
    }

    public JSONObject echo(CardinalDirection dir) {
        return droneActions.echo(dir);
    }

    public void setHeading(CardinalDirection direction) {
        this.direction = direction;
    }

    public void scan() {
        update(droneActions.scan());
    }

    public void addCreek(String id, int x, int y) {
        creeks.put(id, new int[]{x, y});
    }

    public void addSite(String id, int x, int y) {
        sites.put(id, new int[]{x, y});
    }

    public Map<String, int[]> getCreeks() {
        return creeks;
    }

    public Map<String, int[]> getSites() {
        return sites;
    }
}
