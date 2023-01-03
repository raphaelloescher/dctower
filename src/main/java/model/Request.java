package model;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode
public class Request {
    @NonNull
    private int currentFloor;
    @NonNull
    private int destinationFloor;
    private Direction direction;

    public Request(int currentFloor, int destinationFloor) {
        if (currentFloor == destinationFloor) {
            throw new IllegalArgumentException("Elevator can only go up or down");
        }
        this.currentFloor = currentFloor;
        this.destinationFloor = destinationFloor;
        this.direction = destinationFloor - currentFloor > 0 ? Direction.UP : Direction.DOWN;
    }
}
