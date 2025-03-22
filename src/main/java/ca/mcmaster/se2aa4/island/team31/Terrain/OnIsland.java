package ca.mcmaster.se2aa4.island.team31.Terrain;

import org.json.JSONArray;
import org.json.JSONObject;

import ca.mcmaster.se2aa4.island.team31.Drone.Sensor;
import ca.mcmaster.se2aa4.island.team31.Enums.Direction.CardinalDirection;
import ca.mcmaster.se2aa4.island.team31.Interfaces.Actions;

import java.util.HashMap;
import java.util.Map;

public class OnIsland extends State implements Decidable {

    private boolean shouldDecide;
    private final JSONObject[] echoResults = new JSONObject[3];
    private final String[] logicalDirections = { "RIGHT", "FRONT", "LEFT" };

    private final Map<CardinalDirection, Map<String, CardinalDirection>> directionMap = new HashMap<>();

    private int echoIndex = 0;

    public OnIsland(Actions drone, Sensor sensor) {
        super(drone, sensor);
        this.shouldDecide = false;
        initDirectionMap();
    }

    @Override
    public String takeDecision() {
        while (echoIndex < logicalDirections.length) {
            CardinalDirection facing = drone.getDirection();
            String logical = logicalDirections[echoIndex];
            CardinalDirection echoDir = getCardinalDirection(facing, logical);

            if (isBehind(facing, echoDir)) {
                echoIndex++; // Skip illegal echo
                continue;
            }

            JSONObject echoCmd = drone.echo(echoDir);
            echoIndex++;
            return (echoCmd != null) ? echoCmd.toString() : null;
        }

        shouldDecide = true;
        return null;
    }


    @Override
    public void acknowledgeResults(JSONObject response) {
        if (echoIndex > 0 && echoIndex <= logicalDirections.length) {
            echoResults[echoIndex - 1] = response;
        }
        if (echoIndex >= logicalDirections.length) {
            shouldDecide = true;
        }

        JSONObject extras = response.optJSONObject("extras");
        if (extras != null) {
            JSONArray creeks = extras.optJSONArray("creeks");
            if (creeks != null) {
                for (int i = 0; i < creeks.length(); i++) {
                    sensor.addCreek(creeks.optString(i), drone.getX(), drone.getY());
                }
            }
            JSONArray sites = extras.optJSONArray("sites");
            if (sites != null) {
                for (int i = 0; i < sites.length(); i++) {
                    sensor.addSite(sites.optString(i), drone.getX(), drone.getY());
                }
            }
        }
    }

    @Override
    public State getNextState(JSONObject response) {
        return this;
    }

    private void decideAndMove() {
        String right = getEchoResult("RIGHT");
        String front = getEchoResult("FRONT");
        String left = getEchoResult("LEFT");

        if (isWater(right) && isGround(front)) {
            drone.moveForward();
        } else if (isWater(front) && isGround(left)) {
            drone.turnLeft();
        } else if (isWater(left) && isGround(right)) {
            drone.turnRight();
        } else if (isGround(front)) {
            drone.moveForward();
        } else {
            drone.turnLeft();
        }

        drone.scan();
    }

    private String getEchoResult(String logical) {
        int idx = switch (logical) {
            case "RIGHT" -> 0;
            case "FRONT" -> 1;
            case "LEFT"  -> 2;
            default      -> -1;
        };
        if (idx == -1 || echoResults[idx] == null) return "";
        JSONObject extras = echoResults[idx].optJSONObject("extras");
        return (extras != null) ? extras.optString("found", "") : "";
    }

    private boolean isWater(String result) {
        return "OCEAN".equals(result) || "OUT_OF_RANGE".equals(result);
    }

    private boolean isGround(String result) {
        return "GROUND".equals(result);
    }

    private void resetState() {
        shouldDecide = false;
        echoIndex = 0;
        echoResults[0] = null;
        echoResults[1] = null;
        echoResults[2] = null;
    }

    private void initDirectionMap() {
        directionMap.put(CardinalDirection.N, Map.of(
            "LEFT",  CardinalDirection.W,
            "FRONT", CardinalDirection.N,
            "RIGHT", CardinalDirection.E
        ));
        directionMap.put(CardinalDirection.E, Map.of(
            "LEFT",  CardinalDirection.N,
            "FRONT", CardinalDirection.E,
            "RIGHT", CardinalDirection.S
        ));
        directionMap.put(CardinalDirection.S, Map.of(
            "LEFT",  CardinalDirection.E,
            "FRONT", CardinalDirection.S,
            "RIGHT", CardinalDirection.W
        ));
        directionMap.put(CardinalDirection.W, Map.of(
            "LEFT",  CardinalDirection.S,
            "FRONT", CardinalDirection.W,
            "RIGHT", CardinalDirection.N
        ));
    }

    private CardinalDirection getCardinalDirection(CardinalDirection facing, String logical) {
        Map<String, CardinalDirection> subMap = directionMap.getOrDefault(facing, Map.of());
        return subMap.getOrDefault(logical, facing);
    }

    private boolean isBehind(CardinalDirection current, CardinalDirection target) {
        return (current == CardinalDirection.N && target == CardinalDirection.S)
            || (current == CardinalDirection.S && target == CardinalDirection.N)
            || (current == CardinalDirection.E && target == CardinalDirection.W)
            || (current == CardinalDirection.W && target == CardinalDirection.E);
    }
}
