package ca.mcmaster.se2aa4.island.team31.SearchStates;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;

import ca.mcmaster.se2aa4.island.team31.Report;
import ca.mcmaster.se2aa4.island.team31.AbstractClasses.SearchStates;
import ca.mcmaster.se2aa4.island.team31.Detection.GroundSensor;
import ca.mcmaster.se2aa4.island.team31.Drone.Sensor;
import ca.mcmaster.se2aa4.island.team31.Interfaces.Actions;
import ca.mcmaster.se2aa4.island.team31.SearchStates.HelperClasses.SearchPattern;

public class MoveDiagonalState extends SearchStates {

    private static final Logger logger = LogManager.getLogger(MoveDiagonalState.class);

    private final GroundSensor landDetector = new GroundSensor();
    
    private final SearchPattern searchPattern;

    public MoveDiagonalState(Actions drone, Sensor sensor, Report report) {
        super(drone, sensor, report);
        this.searchPattern = new SearchPattern(drone, sensor);
    }
    
    //zig-zag search pattern to find the island
    @Override
    public SearchStates getNextSearch(JSONObject command) {
        if (landDetector.foundGround(command)) {
            logger.info("Found land! Switching to GoToIsland state");
            return new ApproachIslandState(drone, sensor, report, landDetector.getDistance(command));
        }

        searchPattern.executeNextMove();
        return this;
    }
}
