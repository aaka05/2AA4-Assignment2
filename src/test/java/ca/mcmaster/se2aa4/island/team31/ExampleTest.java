package ca.mcmaster.se2aa4.island.team31;

import java.io.File;
import java.lang.reflect.Field;

import org.json.JSONArray;
import org.json.JSONObject;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ca.mcmaster.se2aa4.island.team31.AbstractClasses.SearchStates;
import ca.mcmaster.se2aa4.island.team31.Detection.GroundSensor;
import ca.mcmaster.se2aa4.island.team31.Drone.Constraints;
import ca.mcmaster.se2aa4.island.team31.Drone.MovementController;
import ca.mcmaster.se2aa4.island.team31.Drone.Sensor;
import ca.mcmaster.se2aa4.island.team31.SearchStates.MoveDiagonalState;
import ca.mcmaster.se2aa4.island.team31.SearchStates.ApproachIslandState;
import ca.mcmaster.se2aa4.island.team31.SearchStates.OnIslandState;
import ca.mcmaster.se2aa4.island.team31.SearchStates.IslandRelocationState;

import static eu.ace_design.island.runner.Runner.run;
public class ExampleTest {

    private MovementController drone;
    private Sensor sensor;
    private GroundSensor landDetector;
    private Report report;
    private Constraints constraints;
    private OnIslandState onIsland;


    @BeforeEach
    public void setup() throws Exception {
        // Initialize drone components with starting direction East
        sensor = new Sensor(Direction.CardinalDirection.E);
        drone = new MovementController(7000, Direction.CardinalDirection.E, sensor);
        landDetector = new GroundSensor();
        report = Report.getInstance();
        constraints = new Constraints(drone);
        onIsland = new OnIslandState(drone, sensor, report);
    
        // Need to reset the Report singleton between tests to avoid state bleeding
        Field instance = Report.class.getDeclaredField("instance");
        instance.setAccessible(true);
        instance.set(null, null);
    }
    


    @Test
    public void sampleTest() {
        assertTrue(1 == 1);
    }


    //SensorTest

    @Test
    public void testEchoRight() {
        Sensor sensor = new Sensor(Direction.CardinalDirection.N);
        assertDoesNotThrow(sensor::echoRight);  // Make sure echo doesn't crash
    }

    @Test
    public void testEchoLeft() {
        Sensor sensor = new Sensor(Direction.CardinalDirection.N);
        assertDoesNotThrow(sensor::echoLeft);
    }

    @Test
    public void testScanCommand() {
        Sensor sensor = new Sensor(Direction.CardinalDirection.N);
        assertDoesNotThrow(sensor::scan);
    }

    @Test
    public void testHeadingUpdate() {
        Sensor sensor = new Sensor(Direction.CardinalDirection.N);
        sensor.setHeading(Direction.CardinalDirection.W);
        sensor.echoForward();  // should now echo west
    }

    // TEST: MovementController
    @Test
    public void testTurnRightDirection() {
        drone.turnRight();
        Direction.CardinalDirection dir = drone.getDirection();
        assertEquals(Direction.CardinalDirection.S, dir);
    }
    @Test
    public void testTurnLeftDirection() {
        drone.turnLeft();
        Direction.CardinalDirection dir = drone.getDirection();
        assertEquals(Direction.CardinalDirection.N, dir);
    }

    @Test
    public void testMoveForwardPosition() {
        // Track starting position
        int startX = drone.getX();
        int startY = drone.getY();

        drone.moveForward();

        // Should move one unit east (x+1) since starting direction is east
        int newX = drone.getX();
        int newY = drone.getY();

        assertEquals(startX + 1, newX);
        assertEquals(startY, newY);
    }

    // TEST: LandDetector
    @Test
    public void testLandDetectionTrue() {
        // Mock response from sensor when ground is detected
        JSONObject response = new JSONObject()
                .put("extras", new JSONObject()
                        .put("found", "GROUND")
                        .put("range", 3));

        boolean foundGround = landDetector.foundGround(response);
        int distance = landDetector.getDistance(response);

        assertTrue(foundGround);
        assertEquals(3, distance);
    }

    
    @Test
    public void testLandDetectionFalse() {
        JSONObject response = new JSONObject()
                .put("extras", new JSONObject()
                        .put("found", "OUT_OF_RANGE"));

        boolean foundGround = landDetector.foundGround(response);
        assertFalse(foundGround);
    }

    // TEST: Constraints
    @Test
    public void testCanContinueTrue() {
        boolean canContinue = constraints.canContinue();
        assertTrue(canContinue);
    }

        // TEST: FindIsland state transition
    @Test
    public void testFindIslandReturnsGoToIsland() {
        MoveDiagonalState state = new MoveDiagonalState(drone, sensor, report);
        JSONObject response = new JSONObject()
                .put("extras", new JSONObject().put("found", "GROUND").put("range", 2));
        SearchStates next = state.getNextSearch(response);
        assertTrue(next instanceof ApproachIslandState);
    }

    @Test
    public void testFindIslandReturnsSelfWhenNoLand() {
        MoveDiagonalState state = new MoveDiagonalState(drone, sensor, report);
        JSONObject response = new JSONObject()
                .put("extras", new JSONObject().put("found", "OUT_OF_RANGE"));
        SearchStates next = state.getNextSearch(response);
        assertEquals(state.getClass(), next.getClass());
    }

    // TEST: GoToIsland behavior
    @Test
    public void testGoToIslandProgresses() {
        // Should transition: GoToIsland -> GoToIsland -> GoToIsland -> OnIsland
        // (takes 3 steps to reach island when distance is 2)
        ApproachIslandState goTo = new ApproachIslandState(drone, sensor, report, 2);
        JSONObject dummy = new JSONObject();
    
        SearchStates next1 = goTo.getNextSearch(dummy);
        assertTrue(next1 instanceof ApproachIslandState);
    
        SearchStates next2 = next1.getNextSearch(dummy);
        assertTrue(next2 instanceof ApproachIslandState);  
    
        SearchStates next3 = next2.getNextSearch(dummy);
        assertTrue(next3 instanceof OnIslandState);   
    }


    
    
    // TEST: ReFindIsland state transitions
    @Test
    public void testReFindIslandTransitionsAfterLandRight() {
    IslandRelocationState state = new IslandRelocationState(drone, sensor, report);
    state.getNextSearch(new JSONObject()); // echoRight
    JSONObject landRight = new JSONObject()
        .put("extras", new JSONObject().put("found", "GROUND").put("range", 1));
    SearchStates next = state.getNextSearch(landRight);
    assertEquals(IslandRelocationState.class, next.getClass()); // after turn
    }


    // Simulate ocean detection and check transition
    @Test
    public void testExitingIslandTransition() {
        JSONObject scanResponse = new JSONObject(); // First: scan step
        onIsland.getNextSearch(scanResponse); // triggers scan
        JSONObject flyResponse = new JSONObject()
            .put("extras", new JSONObject()
                .put("biomes", new JSONArray().put("OCEAN")));

        SearchStates result = onIsland.getNextSearch(flyResponse);
        assertTrue(result instanceof OnIslandState); // should echoForward and wait
    }

    // 🔁 Revisit logic triggers moveForward
    @Test
    public void testRevisitTriggersMove() {
        drone.hasVisitedLocation(); // mark this location as visited
        JSONObject scanResponse = new JSONObject(); // dummy input
        SearchStates nextAfter = onIsland.getNextSearch(scanResponse); // should moveForward due to revisit

        assertTrue(nextAfter instanceof OnIslandState); // still in same state
    }


    // TEST: Report
    @Test
    public void testReportAddsAndFindsPOIs() {
    Report report = Report.getInstance();
    report.addCreek("c1", 0, 0);
    report.addSite("s1", 3, 4);

    assertEquals(1, report.getCreeks().length());
    assertEquals(1, report.getSites().length());
    assertTrue(report.isValid());
    assertEquals("c1", report.getClosestCreekToSite());
    }

    


    @Test
    public void map03Test() {
        // Test full exploration on map03 with specific parameters
        String filename = "./maps/map03.json";
        try {
            run(Explorer.class)
                    .exploring(new File(filename))
                    .withSeed(42L)
                    .startingAt(1, 1, "EAST")
                    .backBefore(10000)
                    .withCrew(5)
                    .collecting(1000, "WOOD")
                    .storingInto("./outputs")
                    .withName("Island")
                    .fire();
        } catch (Exception e) {
            System.err.println(e.getMessage());
            e.printStackTrace(System.err);
            System.exit(1);
        }
    }

}


