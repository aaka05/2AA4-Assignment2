package ca.mcmaster.se2aa4.island.team31.Interfaces;


import org.json.JSONObject;

import ca.mcmaster.se2aa4.island.team31.Enums.Direction;
import ca.mcmaster.se2aa4.island.team31.Enums.Direction.CardinalDirection;


public interface Actions {

    public void moveForward();

    public void turnLeft();

    public void turnRight();

    public void stop();

    public int getBatteryLevel();

    public void useBattery(int batteryLevel);

    public int getInitialBatteryLevel();

    public int getX();

    public int getY();

    JSONObject scan();

    JSONObject echo(CardinalDirection direction);





    public Direction.CardinalDirection getDirection();


}

