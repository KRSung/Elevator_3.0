package cecs277.passengers;

import cecs277.Simulation;
import cecs277.elevators.Elevator;
import cecs277.events.PassengerNextDestinationEvent;

public class DistractedDebarking implements DebarkingStrategy {

    boolean missed = false;

    @Override
    public boolean willLeaveElevator(Passenger passenger, Elevator elevator) {
        if (missed){
            return true;
        }
        else {
            if (elevator.getCurrentFloor().getNumber() == passenger.getDestination()) {
                missed = true;
            }
            return false;
        }
    }

    @Override
    public void departedElevator(Passenger passenger, Elevator elevator) {
        Simulation s = elevator.getBuilding().getSimulation();
        if (elevator.getCurrentFloor().getNumber() == 1){
            //TODO leaves building
            passenger.scheduleEvent(elevator.getCurrentFloor());
        }
        else if (elevator.getCurrentFloor().getNumber() == passenger.getDestination()){
            //TODO schedule next desination event
            passenger.scheduleEvent(elevator.getCurrentFloor());
        }
        else {
            //TODO schedule pasenger to reapear on current floor in 5 sec
            PassengerNextDestinationEvent ev = new PassengerNextDestinationEvent(s.currentTime() + 5, passenger, elevator.getCurrentFloor());
            s.scheduleEvent(ev);
        }
    }
}
