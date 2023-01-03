package model;

import lombok.*;

import java.util.*;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class ElevatorSystem {

    @NonNull
    private List<Elevator> elevatorList;
    private LinkedBlockingQueue<Request> pendingRequests = new LinkedBlockingQueue<>();
    @NonNull
    private Executor elevatorSystemExecutor;

    public void putRequest(Request request) {
        try {
            pendingRequests.put(request);
        } catch (InterruptedException e) {
            e.printStackTrace();
            Thread.currentThread().interrupt();
        }
    }

    // Run within new Thread
    public void runElevatorSystem() {
        elevatorSystemExecutor.execute(() -> {
            while (true) {
                       try {
                           var request = pendingRequests.take();
                           var elevator = getNearestAvailableElevator(request);
                           if (!elevator.isEmpty()) {
                               elevator.get().setCurrentDirection(Direction.MOVING);
                               new Thread(() -> elevator.get().startMoving(request, pendingRequests)).start();
                           } else {
                               pendingRequests.put(request);
                           }
                           Thread.sleep(1);
                       } catch (Exception e) {
                           e.printStackTrace();
                           Thread.currentThread().interrupt();
                       }
                   }
        });
    }

    private Optional<Elevator> getNearestAvailableElevator(Request request) {
        return elevatorList.stream().filter(e -> e.getCurrentDirection() == Direction.IDLE)
                .min(Comparator.comparingInt(e -> Math.abs(e.getCurrentFloor() - request.getCurrentFloor())));
    }
}
