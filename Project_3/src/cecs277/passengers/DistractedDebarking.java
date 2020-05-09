package cecs277.passengers;

import cecs277.elevators.Elevator;

public class DistractedDebarking implements DebarkingStrategy {

    boolean missed = false;

    @Override
    public boolean willLeaveElevator(Passenger passenger, Elevator elevator) {
        if (missed){
            return true;
        }
        else {
            missed = true;
            return false;
        }
    }

    @Override
    public void departedElevator(Passenger passenger, Elevator elevator) {
        if (elevator.getCurrentFloor().getNumber() == passenger.getDestination()){
            //TODO schedule next desination event
        }
        else {
            //TODO schedule pasenger to reapear on current floor in 5 sec
        }
    }
}
