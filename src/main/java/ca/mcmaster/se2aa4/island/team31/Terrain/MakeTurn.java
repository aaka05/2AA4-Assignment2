
package ca.mcmaster.se2aa4.island.team31.Terrain;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;

import ca.mcmaster.se2aa4.island.team31.Drone.Sensor;
import ca.mcmaster.se2aa4.island.team31.Enums.Direction.CardinalDirection;
import ca.mcmaster.se2aa4.island.team31.Interfaces.Actions;
public class MakeTurn extends State {

    private static final Logger logger = LogManager.getLogger(MakeTurn.class);
    private LandDetector landDetector = new LandDetector();

    
    private boolean droneHasTurned;
    private boolean checkLand;
    private boolean turnRight;
    private boolean turnLeft;

    public MakeTurn(Actions drone, Sensor sensor, Report report) {
        super(drone, sensor, report);
        
        // Initialize based on current heading
        turnRight = false;
        turnLeft = false;
        droneHasTurned = false;
        checkLand = false;

        logger.info("** In U-Turn State");

    }

    @Override
    public State getNextState(JSONObject response) {
        if (checkLand) {
            if (landDetector.foundGround(response)) {
                return new GoToIsland(this.drone, this.sensor, this.report, landDetector.getDistance(response));
            } else {
                return new BackToIsland(this.drone, this.sensor, this.report);
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

        else if(drone.getSearchHeading() == CardinalDirection.S || drone.getSearchHeading() == CardinalDirection.E){
            if (drone.getDirection() == CardinalDirection.N || drone.getDirection() == CardinalDirection.E){
                turnRight = true;
                drone.turnRight();
                return this;
            }
            else if (drone.getDirection() == CardinalDirection.S || drone.getDirection() == CardinalDirection.W){
                turnLeft = true;
                drone.turnLeft();
                return this;
            }
        }
        else if(drone.getSearchHeading() == CardinalDirection.N || drone.getSearchHeading() == CardinalDirection.W){
            if (drone.getDirection() == CardinalDirection.S || drone.getDirection() == CardinalDirection.W){
                turnRight = true;
                drone.turnRight();
                return this;
            }
            else if (drone.getDirection() == CardinalDirection.N || drone.getDirection() == CardinalDirection.E){
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
