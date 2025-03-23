package ca.mcmaster.se2aa4.island.team31.Interfaces;


import ca.mcmaster.se2aa4.island.team31.Enums.Direction;


public interface Actions {

    public void moveForward();

    public void turnLeft();

    public void turnRight();

    public void stop();

    public int getCurrentCharge();

    public void useBattery(int batteryLevel);

    public int getMaxCapacity();

    public int getX();

    public int getY();

    public Direction.CardinalDirection getDirection();

    public boolean hasVisitedLocation();

    public boolean isTurnPoint();

    public void markAsTurnPoint();

    public Direction.CardinalDirection getSearchHeading();


}

