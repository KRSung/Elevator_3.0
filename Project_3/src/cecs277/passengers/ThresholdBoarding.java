package cecs277.passengers;

import cecs277.elevators.Elevator;

public class ThresholdBoarding implements BoardingStrategy {

    int mThreshold;

    public ThresholdBoarding(int threshold) {
        mThreshold = threshold;
    }

    @Override
    public boolean willBoardElevator(Passenger passenger, Elevator elevator) {
        if (mThreshold > elevator.getPassengerCount()){
            return true;
        }
        else {
            System.out.println(passenger.getName() + " " + passenger.getId() +
                    " won't board elevator " + elevator.getNumber() + " on floor " +
                    elevator.getCurrentFloor() + " because it is above their threshold of " + mThreshold);
            return false;
        }
    }
}
