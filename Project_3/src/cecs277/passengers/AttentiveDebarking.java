package cecs277.passengers;

import cecs277.elevators.Elevator;

public class AttentiveDebarking implements DebarkingStrategy {
    @Override
    public boolean willLeaveElevator(Passenger passenger, Elevator elevator) {
        return elevator.getCurrentFloor().getNumber() == passenger.getDestination();
    }

    @Override
    public void departedElevator(Passenger passenger, Elevator elevator) {
        //TODO schedule next step of travel for passenger
        if (elevator.getCurrentFloor().getNumber() == 1){
            //TODO leaves elevator
            passenger.scheduleEvent(elevator.getCurrentFloor());
        }
        else if (elevator.getCurrentFloor().getNumber() == passenger.getDestination()){
            passenger.scheduleEvent(elevator.getCurrentFloor());
        }
    }
}
