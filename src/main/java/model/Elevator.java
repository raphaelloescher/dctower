package model;

import lombok.*;

import java.util.*;
import java.util.concurrent.LinkedBlockingQueue;

@NoArgsConstructor
@RequiredArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode
public class Elevator {
    @NonNull
    private int currentFloor;
    @NonNull
    private Direction currentDirection;
    @EqualsAndHashCode.Exclude
    private Deque<Integer> stops = new ArrayDeque<>();
    @EqualsAndHashCode.Exclude
    private List<Integer> stopsOfLastRequest = new LinkedList<>();

    public void startMoving(Request request, LinkedBlockingQueue<Request> pendingRequests) {

        if (this.currentFloor != request.getCurrentFloor()) {
            System.out.println("Move to current floor");
            this.currentDirection = request.getCurrentFloor() - this.currentFloor > 0 ? Direction.UP : Direction.DOWN;
            move(pendingRequests, request.getCurrentFloor());
            System.out.println("Arrived at current floor " + this.currentFloor);
        }
        this.stopsOfLastRequest.clear();

        this.stopsOfLastRequest.add(this.currentFloor);

        this.currentDirection = request.getDirection();
        move(pendingRequests, request.getDestinationFloor());
        System.out.println("Arrived at destination floor " + this.currentFloor);
        this.stopsOfLastRequest.add(this.currentFloor);
        // Updating state of Elevator
        this.currentDirection = Direction.IDLE;
    }

    private void move(LinkedBlockingQueue<Request> pendingRequests, int destination) {
        while (this.currentFloor != destination) {
            try {
                if (stops.contains(this.currentFloor)) {
                    stops.pop();

                    System.out.println("Stopover at " + this.currentFloor);

                    // Represents the time for entering and leaving the elevator
                    Thread.sleep(100);

                    this.stopsOfLastRequest.add(this.currentFloor);
                } else {
                    // Represents the time for moving 1 floor
                    Thread.sleep(10);
                }
                moveNextStage();

                // If new Requests --> stopover added
                var pendingRequest = findingPossibleStopover(pendingRequests);
                if (!pendingRequest.isEmpty()) {
                    stops.push(pendingRequest.get().getDestinationFloor());
                    stops.push(pendingRequest.get().getCurrentFloor());
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
                Thread.currentThread().interrupt();
            }
        }
    }

    private void moveNextStage() {
        this.currentFloor = this.currentDirection == Direction.UP ? this.currentFloor + 1 : this.currentFloor - 1;
    }

    private Optional<Request> findingPossibleStopover(LinkedBlockingQueue<Request> pendingRequests) {
        var pendingRequest = pendingRequests.stream()
                .filter(this::isPendingRequestInUpcomingFloors)
                .findFirst();
        if(!pendingRequest.isEmpty()) {
            pendingRequests.remove(pendingRequest.get());
        }
        return pendingRequest;
    }

    private boolean isPendingRequestInUpcomingFloors(Request r) {
        return (r.getDirection() == this.currentDirection &&
                ((r.getCurrentFloor() > this.currentFloor
                        && this.currentDirection == Direction.UP)
                        || (r.getCurrentFloor() < this.currentFloor
                        && this.currentDirection == Direction.DOWN)));
    }

}
