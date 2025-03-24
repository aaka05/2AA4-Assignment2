package ca.mcmaster.se2aa4.island.team31.SearchStates;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;

import ca.mcmaster.se2aa4.island.team31.Report;
import ca.mcmaster.se2aa4.island.team31.AbstractClasses.State;
import ca.mcmaster.se2aa4.island.team31.Detection.LandDetector;
import ca.mcmaster.se2aa4.island.team31.Drone.Sensor;
import ca.mcmaster.se2aa4.island.team31.Interfaces.Actions;

public class FindIsland extends State {

    private static final Logger logger = LogManager.getLogger(FindIsland.class);

    private final LandDetector landDetector = new LandDetector();
    
    private final SearchPattern searchPattern;

    public FindIsland(Actions drone, Sensor sensor, Report report) {
        super(drone, sensor, report);
        this.searchPattern = new SearchPattern(drone, sensor);
    }
    
    //zig-zag search pattern to find the island
    @Override
    public State getNextState(JSONObject command) {
        if (landDetector.foundGround(command)) {
            logger.info("Found land! Switching to GoToIsland state");
            return new GoToIsland(drone, sensor, report, landDetector.getDistance(command));
        }

        searchPattern.executeNextMove();
        return this;
    }
}
