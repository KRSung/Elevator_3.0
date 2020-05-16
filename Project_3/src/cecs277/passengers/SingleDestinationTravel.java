package cecs277.passengers;

import cecs277.Simulation;
import cecs277.buildings.Floor;
import cecs277.elevators.Elevator;
import cecs277.events.PassengerNextDestinationEvent;

public class SingleDestinationTravel implements TravelStrategy {

    private final Simulation mSimulation;
    private int mDestinationFloor, mDurationTime;

    public SingleDestinationTravel(int destinationFloor, int durationTime, Simulation sim) {
        this.mDestinationFloor = destinationFloor;
        this.mDurationTime = durationTime;
        this.mSimulation = sim;
    }
    
    @Override
    public int getDestination() {
        return mDestinationFloor;
    }

    @Override
    public void scheduleNextDestination(Passenger passenger, Floor currentFloor) {
        if (currentFloor.getNumber() != 1){
            mDestinationFloor = 1;
            PassengerNextDestinationEvent ev = new PassengerNextDestinationEvent(mSimulation.currentTime() + mDurationTime, passenger, currentFloor);
            mSimulation.scheduleEvent(ev);
        }
    }
}
