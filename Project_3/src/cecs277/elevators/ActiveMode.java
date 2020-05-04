package cecs277.elevators;

import cecs277.buildings.Building;
import cecs277.buildings.Floor;

/**
 * An ActiveMode elevator is handling at least one floor request.
 */
public class ActiveMode implements OperationMode {
	
	// TODO: implement this class.
	// An active elevator cannot be dispatched, and will ignore direction requests from its current floor. (Only idle
	//    mode elevators observe floors, so an ActiveMode elevator will never observe directionRequested.)
	// The bulk of your Project 2 tick() logic goes here, except that you will never be in IDLE_STATE when active.
	// If you used to schedule a transition to IDLE_STATE, you should instead schedule an operation change to
	//    IdleMode in IDLE_STATE.
	// Otherwise your code should be almost identical, except you are no longer in the Elevator class, so you need
	//    to use accessors and mutators instead of directly addressing the fields of Elevator.
	
	
	
	@Override
	public String toString() {
		return "Active";
	}

	@Override
	public boolean canBeDispatchedToFloor(Elevator elevator, Floor floor) {
		return false;
	}

	@Override
	public void dispatchToFloor(Elevator elevator, Floor targetFloor, Elevator.Direction targetDirection) {

	}

	@Override
	public void directionRequested(Elevator elevator, Floor floor, Elevator.Direction direction) {

	}

	@Override
	public void tick(Elevator elevator) {

	}
}
