package cecs277.passengers;

import cecs277.elevators.Elevator;

public class ConfusedDebarking implements DebarkingStrategy {
    @Override
    public boolean willLeaveElevator(Passenger passenger, Elevator elevator) {
        return elevator.getCurrentFloor().getNumber() == 1;
    }

    @Override
    public void departedElevator(Passenger passenger, Elevator elevator) {
        //TODO leaves the building
        if (elevator.getCurrentFloor().getNumber() == 1){
            //TODO leaves the building
            passenger.scheduleEvent(elevator.getCurrentFloor());
        }
    }
}
