
package cecs277.passengers;

import cecs277.Simulation;
import cecs277.elevators.Elevator;
import cecs277.events.PassengerNextDestinationEvent;

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
                System.out.println(passenger.getName() + " " + passenger.getId() + " is distracted and missed their stop on floor " + elevator.getCurrentFloor().getNumber());
                missed = true;
            }
            return false;
        }
    }

    @Override
    public void departedElevator(Passenger passenger, Elevator elevator) {
        Simulation s = elevator.getBuilding().getSimulation();
        if (elevator.getCurrentFloor().getNumber() == 1 && passenger.getDestination() == 1){
            //TODO leaves building
            passenger.scheduleEvent(elevator.getCurrentFloor());
        }
        else if (elevator.getCurrentFloor().getNumber() == passenger.getDestination()){
            System.out.println(passenger.getName() + " " + passenger.getId() + " finally debarked at their destination " + passenger.getDestination());
            //TODO schedule next desination event
            passenger.scheduleEvent(elevator.getCurrentFloor());
        }
        else {
            //TODO schedule pasenger to reapear on current floor in 5 sec
            System.out.println(passenger.getName() + " " + passenger.getId() +
                    " got of the elevator " + elevator.getNumber() + " on the wrong floor!");
            PassengerNextDestinationEvent ev = new PassengerNextDestinationEvent(s.currentTime() + 5, passenger, elevator.getCurrentFloor());
            s.scheduleEvent(ev);
        }
    }
}