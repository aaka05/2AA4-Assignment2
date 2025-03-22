
package ca.mcmaster.se2aa4.island.team31.Terrain;

import org.json.JSONObject;

import ca.mcmaster.se2aa4.island.team31.Drone.Sensor;
import ca.mcmaster.se2aa4.island.team31.Enums.Direction.CardinalDirection;
import ca.mcmaster.se2aa4.island.team31.Interfaces.Actions;

public class MakeTurn extends State {
    private LandDetector landDetector = new LandDetector();

    
    private boolean droneHasTurned;
    private boolean checkLand;
    private boolean turnRight;
    private boolean turnLeft;

    public MakeTurn(Actions drone, Sensor sensor) {
        super(drone, sensor);
        
        // Initialize based on current heading
        turnRight = false;
        turnLeft = false;
        droneHasTurned = false;
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
        else if(droneHasTurned){
            checkLand = true;
            sensor.echoForward();
            return this;
        }
        else if (turnRight) {
            drone.turnRight();
            droneHasTurned = true;
            return this;
        }
        else if (turnLeft) {
            drone.turnLeft();
            droneHasTurned = true;
            return this;
        }

        else if(drone.getDirection() == CardinalDirection.S || drone.getDirection() == CardinalDirection.E){
            if (drone.getSearchHeading() == CardinalDirection.N || drone.getSearchHeading() == CardinalDirection.E){
                turnRight = true;
                drone.turnRight();
                return this;
            }
            else if (drone.getSearchHeading() == CardinalDirection.S || drone.getSearchHeading() == CardinalDirection.W){
                turnLeft = true;
                drone.turnLeft();
                return this;
            }
        }
        else if(drone.getDirection() == CardinalDirection.N || drone.getDirection() == CardinalDirection.W){
            if (drone.getSearchHeading() == CardinalDirection.S || drone.getSearchHeading() == CardinalDirection.W){
                turnRight = true;
                drone.turnRight();
                return this;
            }
            else if (drone.getSearchHeading() == CardinalDirection.N || drone.getSearchHeading() == CardinalDirection.E){
                turnLeft = true;
                drone.turnLeft();
                return this;
            }
        }
        else{
            throw new IllegalStateException("Error: Invalid State/Direction");
        }
        return null;
    }
}
