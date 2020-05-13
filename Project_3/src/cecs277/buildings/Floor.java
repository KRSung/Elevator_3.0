package cecs277.buildings;

import cecs277.elevators.Elevator;
import cecs277.elevators.ElevatorObserver;
import cecs277.passengers.Passenger;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class Floor implements ElevatorObserver {
	private Building mBuilding;
	private List<Passenger> mPassengers = new ArrayList<>();
	private ArrayList<FloorObserver> mObservers = new ArrayList<>();
	private int mNumber;
	
	// Done: declare a field(s) to help keep track of which direction buttons are currently pressed.
	private boolean upButtonPressed = false;
	private boolean downButtonPressed = false;
	// You can assume that every floor has both up and down buttons, even the ground and top floors.
	
	public Floor(int number, Building building) {
		mNumber = number;
		mBuilding = building;
	}
	
	
	/**
	 * Sets a flag that the given direction has been requested by a passenger on this floor. If the direction
	 * had NOT already been requested, then all observers of the floor are notified that directionRequested.
	 * @param direction
	 */
	public void requestDirection(Elevator.Direction direction) {
		if (direction == Elevator.Direction.MOVING_UP && !upButtonPressed) {
			upButtonPressed = true;
			ArrayList<FloorObserver> temp = new ArrayList<>(mObservers);
			for( FloorObserver floor: temp ){
//			for (int i = 0; i < mObservers.size(); i++){
				floor.directionRequested(this, Elevator.Direction.MOVING_UP);
			}
		}
		else if (direction == Elevator.Direction.MOVING_DOWN && !downButtonPressed) {
			downButtonPressed = true;
			ArrayList<FloorObserver> temp = new ArrayList<>(mObservers);
			for( FloorObserver floor: temp ){
//			for (int i = 0; i < mObservers.size(); i++){
//				mObservers.get(i).directionRequested(this, Elevator.Direction.MOVING_UP);
				floor.directionRequested(this, Elevator.Direction.MOVING_DOWN);
			}
		}

		// Done: implement this method as described in the comment.
	}
	
	/**
	 * Returns true if the given direction button has been pressed.
	 */
	public boolean directionIsPressed(Elevator.Direction direction) {
		if ((upButtonPressed && Elevator.Direction.MOVING_UP == direction) ||
				(downButtonPressed && Elevator.Direction.MOVING_DOWN == direction)){
			return true;
		}
		return false;
		// Done: complete this method.
	}
	
	/**
	 * Clears the given direction button so it is no longer pressed.
	 */
	public void clearDirection(Elevator.Direction direction) {
		if (direction == Elevator.Direction.MOVING_UP) {
			upButtonPressed = false;
		}
		else if (direction == Elevator.Direction.MOVING_DOWN) {
			downButtonPressed = false;
		}
		// Done: complete this method.
	}
	
	/**
	 * Adds a given Passenger as a waiting passenger on this floor, and presses the passenger's direction button.
	 */
	public void addWaitingPassenger(Passenger p) {
		mPassengers.add(p);
		addObserver(p);
		p.setState(Passenger.PassengerState.WAITING_ON_FLOOR);
		int pDestination = p.getDestination();

		if (pDestination > getNumber()){
			requestDirection(Elevator.Direction.MOVING_UP);
		}
		else{
			requestDirection(Elevator.Direction.MOVING_DOWN);
		}
		
		// Done: call requestDirection with the appropriate direction for this passenger's destination.
	}
	
	/**
	 * Removes the given Passenger from the floor's waiting passengers.
	 */
	public void removeWaitingPassenger(Passenger p) {
		mPassengers.remove(p);
	}
	
	
	// Simple accessors.
	public int getNumber() {
		return mNumber;
	}

	public boolean getUpButtonPressed(){
		return upButtonPressed;
	}

	public boolean getDownButtonPressed(){
		return downButtonPressed;
	}
	
	public List<Passenger> getWaitingPassengers() {
		return mPassengers;
	}

	@Override
	public String toString() {
		return "Floor " + mNumber;
	}
	
	// Observer methods.
	public void removeObserver(FloorObserver observer) {
		mObservers.remove(observer);
	}
	
	public void addObserver(FloorObserver observer) {
		mObservers.add(observer);
	}
	
	// Observer methods.
	@Override
	public void elevatorDecelerating(Elevator elevator) {
		// Done: if the elevator is arriving at THIS FLOOR, alert all the floor's observers that elevatorArriving.
		if (elevator.getCurrentFloor() == this) {
			ArrayList<FloorObserver> temp = mObservers;
			for (FloorObserver o : temp) {
				o.elevatorArriving(this, elevator);
			}
			// Done: then clear the elevator's current direction from this floor's requested direction buttons.
			clearDirection(elevator.getCurrentDirection());
		}
	}
	
	@Override
	public void elevatorDoorsOpened(Elevator elevator) {
		// Not needed.
	}
	
	@Override
	public void elevatorWentIdle(Elevator elevator) {
		// Not needed.
	}
}
