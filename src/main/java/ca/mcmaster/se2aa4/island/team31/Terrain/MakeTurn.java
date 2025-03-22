
package ca.mcmaster.se2aa4.island.team31.Terrain;

import org.json.JSONObject;

import ca.mcmaster.se2aa4.island.team31.Drone.Sensor;
import ca.mcmaster.se2aa4.island.team31.Enums.Direction.CardinalDirection;
import ca.mcmaster.se2aa4.island.team31.Interfaces.Actions;

public class MakeTurn extends State {
    private LandDetector landDetector = new LandDetector();

    private boolean turnRight;
    private boolean turnLeft;
    private boolean turnDone;
    private boolean checkLand;

    public MakeTurn(Actions drone, Sensor sensor) {
        super(drone, sensor);
        
        // Initialize based on current heading
        CardinalDirection heading = drone.getDirection();
        turnRight = (heading == CardinalDirection.N || heading == CardinalDirection.E);
        turnLeft = (heading == CardinalDirection.S || heading == CardinalDirection.W);
        turnDone = false;
        checkLand = false;
    }

    public State getNextState(JSONObject response) {
        if (checkLand) {
            if (landDetector.foundGround(response)) {
                return new GoToIsland(this.drone, this.sensor, landDetector.getDistance(response));
            } else {
                return new BackToIsland(this.drone, this.sensor);
            }
        }
        
        if (turnDone) {
            checkLand = true;
            sensor.echoForward();
            return this;
        }
        
        if (turnRight) {
            drone.turnRight();
            turnDone = true;
            return this;
        }
        
        if (turnLeft) {
            drone.turnLeft();
            turnDone = true;
            return this;
        }

        // Should never reach here if state is properly initialized
        throw new IllegalStateException("Invalid turn state reached");
    }
}
