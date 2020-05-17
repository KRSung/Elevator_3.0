
package cecs277.passengers;

import cecs277.Simulation;
import cecs277.elevators.Elevator;
import cecs277.events.PassengerNextDestinationEvent;
import cecs277.logging.Logger;

public class DistractedDebarking implements DebarkingStrategy {

    boolean missed = false;
    boolean debarked = false;

    @Override
    public boolean willLeaveElevator(Passenger passenger, Elevator elevator) {
        if (missed && !debarked){
            debarked = true;
            return true;
        }
        else if (passenger.getDestination() == elevator.getCurrentFloor().getNumber() && debarked){
            return true;
        }
        else {
            if (elevator.getCurrentFloor().getNumber() == passenger.getDestination()) {
                Logger.getInstance().logString(passenger.getName() + " " + passenger.getId() 
                        + " is distracted and missed their stop on floor " + elevator.getCurrentFloor().getNumber());
                missed = true;
            }
            return false;
        }
    }

    @Override
    public void departedElevator(Passenger passenger, Elevator elevator) {
        Simulation s = elevator.getBuilding().getSimulation();
        if (elevator.getCurrentFloor().getNumber() == 1 && passenger.getDestination() == 1){
            passenger.scheduleEvent(elevator.getCurrentFloor());
        }
        else if (elevator.getCurrentFloor().getNumber() == passenger.getDestination()){
            Logger.getInstance().logString(passenger.getName() + " " + passenger.getId()
                    + " finally debarked at their destination floor " + passenger.getDestination());
            passenger.scheduleEvent(elevator.getCurrentFloor());
        }
        else {
            Logger.getInstance().logString(passenger.getName() + " " + passenger.getId() +
                    " got of the elevator " + elevator.getNumber() + " on the wrong floor!");
            PassengerNextDestinationEvent ev = new PassengerNextDestinationEvent(s.currentTime() + 5, passenger, elevator.getCurrentFloor());
            s.scheduleEvent(ev);
        }
    }
}