package tests;

import model.Direction;
import model.Elevator;
import model.ElevatorSystem;
import model.Request;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.assertEquals;


// If tests fail it could be that the processing time is too low --> enter higher numbers
public class ElevatorTest {

    public static ElevatorSystem elevatorSystem;
    private static List<Elevator> elevators = new ArrayList<Elevator>();

    @BeforeAll
    public static void init() {
        // List of Elevator Examples
        elevators.add(new Elevator(4, Direction.IDLE));
        elevators.add(new Elevator(17, Direction.IDLE));
        elevators.add(new Elevator(38, Direction.IDLE));
        elevators.add(new Elevator(46, Direction.IDLE));
        elevators.add(new Elevator(55, Direction.IDLE));
        elevators.add(new Elevator(13, Direction.IDLE));
        elevators.add(new Elevator(47, Direction.IDLE));

        // Starting Elevator System
        Executor executor = Executors.newSingleThreadExecutor();

        elevatorSystem = new ElevatorSystem(elevators, executor);
        elevatorSystem.runElevatorSystem();
    }

    @Test
    void elevatorStopsAtCorrectFloor() {
        elevatorSystem.setElevatorList(elevators);

        elevatorSystem.putRequest(new Request(0, 35));
        // Time for processing
        try {
            Thread.sleep(2500);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        // Elevator 0 --> the nearest Elevator
        // Entrance 0, Destination 35
        assertEquals(List.of(0, 35), elevatorSystem.getElevatorList().get(0).getStopsOfLastRequest());
    }

    @Test
    void elevatorMakesCorrectStopover() {
        var elevatorList = new ArrayList<Elevator>();
        elevatorList.add(new Elevator( 0, Direction.IDLE));
        elevatorSystem.setElevatorList(elevatorList);

        elevatorSystem.putRequest(new Request(35, 0));
        elevatorSystem.putRequest(new Request(17, 0));

        // Time for processing
        try {
            Thread.sleep(2500);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        // Entrance 35, Stopover 17, Destination 0
        assertEquals(List.of(35, 17, 0), elevatorSystem.getElevatorList().get(0).getStopsOfLastRequest());
    }
}
