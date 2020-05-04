package cecs277.passengers;

import cecs277.buildings.Floor;

public class MultipleDestinationTravel implements TravelStrategy {

    @Override
    public int getDestination() {
        return 0;
    }

    @Override
    public void scheduleNextDestination(Passenger passenger, Floor currentFloor) {

    }
}
