package cecs277.passengers;

import cecs277.elevators.Elevator;

public class AttentiveDebarking implements DebarkingStrategy {
    @Override
    public boolean willLeaveElevator(Passenger passenger, Elevator elevator) {
        return false;
    }

    @Override
    public void departedElevator(Passenger passenger, Elevator elevator) {

    }
}
