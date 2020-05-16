package cecs277.passengers;

import cecs277.Simulation;
import cecs277.buildings.Floor;
import cecs277.events.PassengerNextDestinationEvent;

import java.util.List;

public class MultipleDestinationTravel implements TravelStrategy {

    private final Simulation mSimulation;
    private List<Integer> mDestinations;
    private List<Long> mDurations;

    public MultipleDestinationTravel(List<Integer> destinations, List<Long> durations, Simulation sim){
        mDestinations = destinations;
        mDurations = durations;
        this.mSimulation = sim;
    }

    @Override
    public int getDestination() {
        if (mDestinations.isEmpty()){
            return 1;
        }
        return mDestinations.get(0);
    }

    @Override
    public void scheduleNextDestination(Passenger passenger, Floor currentFloor) {
        if (currentFloor.getNumber() != 1){
            PassengerNextDestinationEvent ev = new PassengerNextDestinationEvent(mSimulation.currentTime() + mDurations.remove(0), passenger, currentFloor);
            mDestinations.remove(0);
            mSimulation.scheduleEvent(ev);
        }
    }
}
