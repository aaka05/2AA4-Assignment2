package ca.mcmaster.se2aa4.island.team31;

import ca.mcmaster.se2aa4.island.team31.Interfaces.DroneActions;

public class Fly implements DroneActions {

    private final DroneController controls;
    public Fly(DroneController controls) {
        this.controls = controls;
    }
    
    @Override
    public void action() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'action'");
    }

    
}
