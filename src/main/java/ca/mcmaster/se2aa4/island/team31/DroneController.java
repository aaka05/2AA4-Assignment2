package ca.mcmaster.se2aa4.island.team31;

import java.util.Arrays;
import java.util.List;

import org.json.JSONObject;

import ca.mcmaster.se2aa4.island.team31.Drone.Constraints;
import ca.mcmaster.se2aa4.island.team31.Drone.MovementController;
import ca.mcmaster.se2aa4.island.team31.Drone.Sensor;
import ca.mcmaster.se2aa4.island.team31.Enums.Direction;
import ca.mcmaster.se2aa4.island.team31.Interfaces.ExplorerDrone;
import ca.mcmaster.se2aa4.island.team31.Terrain.FindIsland;
import ca.mcmaster.se2aa4.island.team31.Terrain.State;

public class DroneController {

    private DroneTracker droneTracker;

    private MovementController drone;
    private Sensor sensor;
    private Constraints constraints;
    private State currentState;
    private JSONObject previousCommand;


    public DroneController(int batteryLevel, String direction) {
        Direction.CardinalDirection startDirection = Direction.CardinalDirection.E;

        try {
            startDirection = Direction.CardinalDirection.valueOf(direction);
        } catch (Exception e) {
            e.printStackTrace();
        }

        this.drone = new MovementController(batteryLevel, direction); //not sure if it should be in initial direction
        this.sensor = new Sensor(startDirection);
        this.constraints = new Constraints(this.drone);


        this.currentState = new FindIsland(this.drone, this.sensor);

        List<ExplorerDrone> subjects = Arrays.asList(this.drone, this.sensor);
        this.droneTracker = new DroneTracker(subjects);
    }

    private void updateBatteryLevel(){
        if (previousCommand != null){
            int batteryLevel = previousCommand.getInt("cost");
            this.drone.useBattery(batteryLevel);
        }
    }

    private JSONObject stopMoving(){
        this.drone.stop();
        return droneTracker.getRecentCommand();
    }

    public JSONObject takeDecision(){
        updateBatteryLevel();

        if(!constraints.enoughBattery()){
            return stopMoving();
        }

        State nextState;
        State currentState;

        do{
            currentState = this.currentState; //keep track of previous state

            nextState = this.currentState.getNextState(previousCommand);

            this.currentState = nextState; //update current state to the next state

        } while (!currentState.equals(nextState));


        return droneTracker.getRecentCommand();
    }

    public void updateDrone(JSONObject command){
        this.previousCommand = command;
    }

}