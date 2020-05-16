package cecs277.passengers;

import cecs277.elevators.Elevator;

public class ResponsibleEmbarking implements EmbarkingStrategy {

    @Override
    public void enteredElevator(Passenger passenger, Elevator elevator) {
        elevator.requestFloor(passenger.getDestination());
        System.out.println(passenger.getName() + " " + passenger.getId() + " requested floor " + passenger.getDestination() + " on elevator " + elevator.getNumber());
        if (elevator.getRequestedFloors()[elevator.getCurrentFloor().getNumber() - 1] == true){
            elevator.getRequestedFloors()[elevator.getCurrentFloor().getNumber() - 1] = false;
        }
    }

}
