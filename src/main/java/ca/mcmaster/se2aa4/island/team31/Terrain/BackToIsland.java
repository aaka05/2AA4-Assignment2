package ca.mcmaster.se2aa4.island.team31.Terrain;

import org.json.JSONObject;

import ca.mcmaster.se2aa4.island.team31.Drone.Sensor;
import ca.mcmaster.se2aa4.island.team31.Interfaces.Actions;

public class BackToIsland extends State {
    private LandDetector landDetector = new LandDetector();
    
    private boolean shouldEcho;
    private boolean echoRight;
    private boolean echoLeft;
    private boolean checkRight;
    private boolean checkLeft;
    private boolean turnComplete;
    private boolean finalCheck;

    public BackToIsland(Actions drone, Sensor sensor) {
        super(drone, sensor);
        shouldEcho = true;
        echoRight = true;
        echoLeft = false;
        checkRight = false;
        checkLeft = false;
        turnComplete = false;
        finalCheck = false;
    }

    @Override
    public State getNextState(JSONObject response) {
        if (finalCheck) {
            if (landDetector.foundGround(response)) {
                return new GoToIsland(drone, sensor, landDetector.getDistance(response));
            }
            return new BackToIsland(drone, sensor);
        }

        if (turnComplete) {
            sensor.echoForward();
            finalCheck = true;
            turnComplete = false;
            return this;
        }

        if (checkRight) {
            if (landDetector.foundGround(response)) {
                drone.turnRight();
                turnComplete = true;
                return this;
            }
            checkRight = false;
        } else if (checkLeft) {
            if (landDetector.foundGround(response)) {
                drone.turnLeft();
                turnComplete = true;
                return this;
            }
            checkLeft = false;
        }

        if (shouldEcho) {
            if (echoRight) {
                sensor.echoRight();
                checkRight = true;
                echoLeft = true;
                echoRight = false;
                return this;
            } else if (echoLeft) {
                sensor.echoLeft();
                shouldEcho = false;
                checkLeft = true;
                echoLeft = false;
                return this;
            }
        }

        return new MakeTurn(drone, sensor);
    }
}


