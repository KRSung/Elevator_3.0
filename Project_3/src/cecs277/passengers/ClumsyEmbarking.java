package cecs277.passengers;

import cecs277.elevators.Elevator;

public class ClumsyEmbarking implements EmbarkingStrategy{
    @Override
    public void enteredElevator(Passenger passenger, Elevator elevator) {
        if (elevator.getCurrentDirection() == Elevator.Direction.MOVING_DOWN || elevator.getCurrentFloor().getNumber() > passenger.getDestination()){
            elevator.requestFloor(passenger.getDestination());
            elevator.requestFloor(passenger.getDestination() + 1);
        }
        else if (elevator.getCurrentDirection() == Elevator.Direction.MOVING_UP || elevator.getCurrentFloor().getNumber() < passenger.getDestination()){
            elevator.requestFloor(passenger.getDestination());
            elevator.requestFloor(passenger.getDestination() - 1);
            System.out.println(passenger.getName() + " " + passenger + " clumsily requested floors " +
                    passenger.getDestination() + " and " + (passenger.getDestination() - 1));
        }
    }
}
