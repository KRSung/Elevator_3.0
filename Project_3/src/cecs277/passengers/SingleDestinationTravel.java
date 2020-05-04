package cecs277.passengers;

import cecs277.Simulation;
import cecs277.buildings.Floor;
import cecs277.elevators.Elevator;
import cecs277.events.PassengerNextDestinationEvent;

public class SingleDestinationTravel implements TravelStrategy {

    private int mDestinationFloor, mDurationTime;

    public SingleDestinationTravel(int destinationFloor, int durationTime) {
        this.mDestinationFloor = destinationFloor;
        this.mDurationTime = durationTime;
    }


    protected void scheduleNextDestination(Elevator elevator) {


    }

    @Override
    public int getDestination() {
        return 0;
    }

    @Override
    public void scheduleNextDestination(Passenger passenger, Floor currentFloor) {
        if (currentFloor.getNumber() == 1){
            System.out.println(passenger.getName() + passenger.getId() + " is leaving the building.");
        } else {
            mDestinationFloor = 1;
            Simulation s = currentFloor.g;
            PassengerNextDestinationEvent ev = new PassengerNextDestinationEvent(s.currentTime() + mDurationTime, passenger, currentFloor);
            s.scheduleEvent(ev);
        }
    }
}
