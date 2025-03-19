package ca.mcmaster.se2aa4.island.team31;

//so this class will be responsible for controlling the drone
import java.util.LinkedList;
import java.util.Queue;

import org.json.JSONObject;

import ca.mcmaster.se2aa4.island.team31.Direction.CardinalDirection;
import ca.mcmaster.se2aa4.island.team31.Drone.Battery;

public class DroneController{
    private Direction.CardinalDirection direction;
    private int batteryLevel;
    int x;
    int y;
    private Queue<JSONObject> actionQueue;
    private Battery battery;



    public DroneController(Integer batteryLevel, String startDirection){
        this.battery = new Battery(batteryLevel);
        this.actionQueue = new LinkedList<>();
        this.x = 0; // Assuming initial position is (0,0), can be changed dynamically
        this.y = 0;
        try{
            this.direction = Direction.CardinalDirection.valueOf(startDirection);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid direction: " + startDirection);

        }
    }

    public void addAction(JSONObject decision) {
            actionQueue.add(decision);
            updateBattery(decision);
        }
        // TODO Auto-generated method stub
        //throw new UnsupportedOperationException("Unimplemented method 'addAction'");

    

        private void updateBattery(JSONObject decision) {
            String action = decision.getString("action");
            int cost = 0;
    
            if (action.equals("fly")) {
                cost = 1;
                moveForward();
            } else if (action.equals("heading")) {
                cost = 1;
                changeDirection(decision.getString("direction"));
            } else if (action.equals("scan")) {
                cost = 2;
            } else if (action.equals("echo")) {
                cost = 1;
            } else if (!action.equals("stop")) {
                throw new IllegalArgumentException("Unknown action: " + action);
            }
    
            battery.useBattery(cost);
        }
    
        private void moveForward() {
            if (direction == CardinalDirection.N) {
                y -= 1;
            } else if (direction == CardinalDirection.S) {
                y += 1;
            } else if (direction == CardinalDirection.E) {
                x += 1;
            } else if (direction == CardinalDirection.W) {
                x -= 1;
            }
        }
    
        //will later break into its own class
        private void changeDirection(String newDirection) {
            try {
                this.direction = CardinalDirection.valueOf(newDirection);
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("Invalid heading direction: " + newDirection);
            }
        }
    
        public boolean shouldReturnHome() {
            return battery.goHome(x, y, 1);
        }
    
        public int getBatteryLevel() {
            return battery.getBatteryLevel();
        }
    
        public CardinalDirection getDirection() {
            return direction;
        }
    
        public int getX() {
            return x;
        }
    
        public int getY() {
            return y;
        }
    
        public JSONObject getNextAction() {
            return actionQueue.poll();
        }

        
        /* Moves the drone diagonally (Southeast) until it reaches land.*/
        public void goInitialDiagonal() {
            direction = CardinalDirection.E; // Start moving East
            boolean moveSouthNext = true; // Alternate between East and South

            while (true) {
                // Check for land using ECHO (replace Echo class with direct JSON command)
                JSONObject echoAction = new JSONObject()
                        .put("action", "echo")
                        .put("parameters", new JSONObject().put("direction", direction));
                addAction(echoAction);

                // Get the response from the game engine
                JSONObject response = getNextAction(); // Processes the response
                JSONObject extras = response.getJSONObject("extras");
                String found = extras.getString("found");

                // Stop moving if land is found
                if ("GROUND".equals(found)) {
                    System.out.println("✅ Land detected.");
                    break;
                }

                // Move diagonally (switching between East and South)
                if (moveSouthNext) {
                    addAction(new JSONObject()
                            .put("action", "heading")
                            .put("parameters", new JSONObject().put("direction", "S")));
                } else {
                    addAction(new JSONObject()
                            .put("action", "heading")
                            .put("parameters", new JSONObject().put("direction", "E")));
                }
                moveSouthNext = !moveSouthNext; // Alternate direction

                // Move forward
                addAction(new JSONObject().put("action", "fly"));
            }
        }


       
    }

    