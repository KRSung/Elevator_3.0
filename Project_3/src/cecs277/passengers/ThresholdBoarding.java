package cecs277.passengers;

import cecs277.elevators.Elevator;

public class ThresholdBoarding implements BoardingStrategy {
    @Override
    public boolean willBoardElevator(Passenger passenger, Elevator elevator) {
        return false;
    }
}
