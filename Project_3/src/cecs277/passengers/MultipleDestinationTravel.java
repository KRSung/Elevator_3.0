package cecs277.passengers;

import cecs277.buildings.Floor;

import java.util.List;

public class MultipleDestinationTravel implements TravelStrategy {

    private List<Integer> mDestinations;
    private List<Long> mDurations;

    public MultipleDestinationTravel(List<Integer> destinations, List<Long> durations){
        //FIXME
        mDestinations = destinations;
        mDurations = durations;
    }

    @Override
    public int getDestination() {
        //FIXME
        if (mDestinations.isEmpty()){
            return 1;
        }
        return mDestinations.get(0);
    }

    @Override
    public void scheduleNextDestination(Passenger passenger, Floor currentFloor) {

    }
}
