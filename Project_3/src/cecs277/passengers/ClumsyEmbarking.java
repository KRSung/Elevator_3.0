package cecs277.passengers;

import cecs277.elevators.Elevator;
import cecs277.logging.Logger;

public class ClumsyEmbarking implements EmbarkingStrategy{
    @Override
    public void enteredElevator(Passenger passenger, Elevator elevator) {
        if (elevator.getCurrentDirection() == Elevator.Direction.MOVING_DOWN || elevator.getCurrentFloor().getNumber() > passenger.getDestination()){
            elevator.requestFloor(passenger.getDestination());
            elevator.requestFloor(passenger.getDestination() + 1);
            Logger.getInstance().logEvent(passenger.getName() + " " + passenger.getId()
                    + " clumsily requested floors " + passenger.getDestination() + " and "
                    + (passenger.getDestination() + 1) + " on elevator " + elevator.getNumber());
        }
        else if (elevator.getCurrentDirection() == Elevator.Direction.MOVING_UP || elevator.getCurrentFloor().getNumber() < passenger.getDestination()){
            elevator.requestFloor(passenger.getDestination());
            elevator.requestFloor(passenger.getDestination() - 1);
            Logger.getInstance().logEvent(passenger.getName() + " " + passenger.getId() + " clumsily requested floors " +
                    passenger.getDestination() + " and " + (passenger.getDestination() - 1) + " on elevator "
                    + elevator.getNumber());
        }
        if (elevator.getRequestedFloors()[elevator.getCurrentFloor().getNumber() - 1] == true){
            elevator.getRequestedFloors()[elevator.getCurrentFloor().getNumber() - 1] = false;
        }
    }
}
