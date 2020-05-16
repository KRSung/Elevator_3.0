package cecs277.passengers;

import cecs277.elevators.Elevator;

public class AwkwardBoarding implements BoardingStrategy {

    int mThreshold;

    public AwkwardBoarding(int threshold) {
        mThreshold = threshold;
    }

    @Override
    public boolean willBoardElevator(Passenger passenger, Elevator elevator) {
        if (mThreshold > elevator.getPassengerCount() && elevator.getPassengerCount() < elevator.getCapacity()){
            return true;
        }
        else {
            mThreshold += 2;
            System.out.println(passenger.getName() + " " + passenger.getId() + " was too awkward to board the elevator on floor " + elevator.getCurrentFloor().getNumber() + ", now has threshold " + mThreshold);
            return false;
        }
    }
}
