package cecs277.passengers;

import cecs277.elevators.Elevator;

/**
 * A CapacityBoarding is a boarding strategy for a Passenger that will get on any elevator that has not reached its
 * capacity.
 */
public class CapacityBoarding implements BoardingStrategy {
	@Override
	public boolean willBoardElevator(Passenger passenger, Elevator elevator) {
		if (elevator.getPassengerCount() < elevator.getCapacity()){
			return true;
		}
		else{
			System.out.println(passenger.getName() + " " + passenger.getId() + " won't board elevator " + elevator.getNumber() + " because it is full.");
			elevator.getCurrentFloor().requestDirection(elevator.getCurrentDirection());
			return false;
		}
	}
}
