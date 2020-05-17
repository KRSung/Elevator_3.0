package cecs277.passengers;

import cecs277.elevators.Elevator;
import cecs277.logging.Logger;

public class ConfusedDebarking implements DebarkingStrategy {
    @Override
    public boolean willLeaveElevator(Passenger passenger, Elevator elevator) {
        return elevator.getCurrentFloor().getNumber() == 1;
    }

    @Override
    public void departedElevator(Passenger passenger, Elevator elevator) {
        if (elevator.getCurrentFloor().getNumber() == 1){
            //TODO leaves the building
            Logger.getInstance().logEvent(passenger.getName() + " " + passenger.getId()
                    + " is confused and left the building after debarking elevator " + elevator.getNumber());
            passenger.scheduleEvent(elevator.getCurrentFloor());
        }
    }
}
