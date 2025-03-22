package ca.mcmaster.se2aa4.island.team31.Terrain;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import ca.mcmaster.se2aa4.island.team31.Drone.Sensor;
import ca.mcmaster.se2aa4.island.team31.Interfaces.Actions;

public class Searcher extends State {

    private static final Logger logger = LogManager.getLogger(Searcher.class);

    private boolean shouldScan;
    private boolean shouldFly;
    private boolean foundLand;

    public Searcher(Actions drone, Sensor sensor) {
        super(drone, sensor);
        shouldScan = true;
        shouldFly = false;
        foundLand = false;
    }

    @Override
    public State getNextState(JSONObject response) {
        if (shouldFly){
            if (inOcean(response)){
                return new BackToIsland(drone, sensor);
            }
            else if(!inOcean(response) && !foundLand){
                foundLand = true;
            }
            else{
                drone.moveForward();
                shouldFly = false;
                shouldScan = true;
            }
        } else if(shouldScan){
            logger.info("Scanning");
            sensor.scan();
            shouldFly = true;
            shouldScan = false;
        }


        return this;
    }

    private boolean inOcean(JSONObject response){
        if (!response.has("extras")){
            return false;
        }
        JSONObject extras = response.getJSONObject("extras");
        if (extras == null || !extras.has("biomes")) return false;

        JSONArray biomes = extras.optJSONArray("biomes");
        return biomes != null && biomes.length() == 1 && "OCEAN".equals(biomes.optString(0));
    }
}




