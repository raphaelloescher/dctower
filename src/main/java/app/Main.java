package app;

import model.Direction;
import model.Elevator;
import model.ElevatorSystem;
import model.Request;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class Main {
    public static void main(String[] args) {
        ElevatorSystem elevatorSystem;

        List<Elevator> elevators = new ArrayList<Elevator>();
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

        elevatorSystem.putRequest(new Request(0, 35));
        elevatorSystem.putRequest(new Request(0, 36));
        elevatorSystem.putRequest(new Request(0, 32));
        elevatorSystem.putRequest(new Request(0, 47));
        elevatorSystem.putRequest(new Request(43, 0));
        elevatorSystem.putRequest(new Request(17, 0));
        elevatorSystem.putRequest(new Request(23, 0));
        elevatorSystem.putRequest(new Request(13, 0));
        elevatorSystem.putRequest(new Request(16, 0));
        elevatorSystem.putRequest(new Request(38, 0));
        elevatorSystem.putRequest(new Request(43, 0));
        elevatorSystem.putRequest(new Request(32, 0));
        elevatorSystem.putRequest(new Request(28, 0));
    }
}
