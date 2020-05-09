package cecs277.passengers;

import cecs277.elevators.Elevator;

public class DisruptiveEmbarking implements EmbarkingStrategy {
    @Override
    public void enteredElevator(Passenger passenger, Elevator elevator) {
        if (elevator.getCurrentDirection() == Elevator.Direction.MOVING_DOWN || elevator.getCurrentFloor().getNumber() > passenger.getDestination()){
            for (int i = passenger.getDestination(); i < 0; i--) {
                elevator.requestFloor(i);
            }
        }
        else if (elevator.getCurrentDirection() == Elevator.Direction.MOVING_UP || elevator.getCurrentFloor().getNumber() < passenger.getDestination()){
            for (int i = passenger.getDestination(); i < elevator.getBuilding().getFloorCount(); i++) {
                elevator.requestFloor(i);
            }
        }
    }
}
