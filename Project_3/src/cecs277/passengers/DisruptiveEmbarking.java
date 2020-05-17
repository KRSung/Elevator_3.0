package cecs277.passengers;

import cecs277.elevators.Elevator;
import cecs277.logging.Logger;

public class DisruptiveEmbarking implements EmbarkingStrategy {
    @Override
    public void enteredElevator(Passenger passenger, Elevator elevator) {
        if (elevator.getCurrentDirection() == Elevator.Direction.MOVING_DOWN || elevator.getCurrentFloor().getNumber() > passenger.getDestination()){
            elevator.requestFloor(passenger.getDestination());
        }
        else if (elevator.getCurrentDirection() == Elevator.Direction.MOVING_UP || elevator.getCurrentFloor().getNumber() < passenger.getDestination()){
            for (int i = passenger.getDestination(); i <= elevator.getBuilding().getFloorCount(); i++) {
                elevator.requestFloor(i);
            }
            Logger.getInstance().logString(passenger.getName() + " " + passenger.getId()
                    + " is being disruptive and requested floor " + passenger.getDestination()
                    + " and everything above it on elevator " + elevator.getNumber() + ".");
        }
        if (elevator.getRequestedFloors()[elevator.getCurrentFloor().getNumber() - 1] == true){
            elevator.getRequestedFloors()[elevator.getCurrentFloor().getNumber() - 1] = false;
        }
    }
}
