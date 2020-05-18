package cecs277.elevators;

import cecs277.buildings.Building;
import cecs277.buildings.Floor;

/**
 * A DispatchMode elevator is in the midst of a dispatch to a target floor in order to handle a request in a target
 * direction. The elevator will not stop on any floor that is not its destination, and will not respond to any other
 * request until it arrives at the destination.
 */
public class DispatchMode implements OperationMode {
	private static boolean canBeDisabled = true;
	private Floor mDestination;
	private Elevator.Direction mDesiredDirection;
	private Floor mCurrentFloor;
	private Building mBuilding;
	private Elevator.Direction mCurrentDirection;
	private Elevator.ElevatorState mCurrentState;

	public DispatchMode(Floor destination, Elevator.Direction desiredDirection) {
		mDestination = destination;
		mDesiredDirection = desiredDirection;
	}
	
	// TODO: implement the other methods of the OperationMode interface.
	// Only Idle elevators can be dispatched.
	// A dispatching elevator ignores all other requests.
	// It does not check to see if it should stop of floors that are not the destination.
	// Its flow of ticks should go: IDLE_STATE -> ACCELERATING -> MOVING -> ... -> MOVING -> DECELERATING.
	//    When decelerating to the destination floor, change the elevator's direction to the desired direction,
	//    announce that it is decelerating, and then schedule an operation change in 3 seconds to
	//    ActiveOperation in the DOORS_OPENING state.
	// A DispatchOperation elevator should never be in the DOORS_OPENING, DOORS_OPEN, or DOORS_CLOSING states.
	
	
	@Override
	public String toString() {
		return "Dispatching to " + mDestination.getNumber() + " " + mDesiredDirection;
	}

	@Override
	public boolean canBeDispatchedToFloor(Elevator elevator, Floor floor) {
		return false;
	}

	@Override
	public void dispatchToFloor(Elevator elevator, Floor targetFloor, Elevator.Direction targetDirection) {	}

	@Override
	public void directionRequested(Elevator elevator, Floor floor, Elevator.Direction direction) { }

	@Override
	public void tick(Elevator elevator) {
		mCurrentFloor = elevator.getCurrentFloor();
		if (mDestination.getNumber() > mCurrentFloor.getNumber()){
			mCurrentDirection = Elevator.Direction.MOVING_UP;
		}
		else{
			mCurrentDirection = Elevator.Direction.MOVING_DOWN;
		}
		mCurrentState = elevator.getCurrentState();
		mCurrentFloor = elevator.getCurrentFloor();
		mBuilding = elevator.getBuilding();

		switch (mCurrentState) {

			case IDLE_STATE:
				if (canBeDisabled && mDestination.getNumber() != 1) {
					canBeDisabled = false;
					elevator.disable(this);
				}
				else{
					elevator.setCurrentDirection(mCurrentDirection);
					elevator.scheduleStateChange(Elevator.ElevatorState.ACCELERATING, 0);
				}
				return;

			case ACCELERATING:
				elevator.getCurrentFloor().removeObserver(elevator);
				elevator.scheduleStateChange(Elevator.ElevatorState.MOVING, 3);
				return;

			case MOVING:
				if (mCurrentDirection == Elevator.Direction.MOVING_UP ) {
					elevator.setCurrentFloor(mBuilding.getFloor(mCurrentFloor.getNumber() + 1));
				}
				else {
					elevator.setCurrentFloor(mBuilding.getFloor(mCurrentFloor.getNumber() - 1));
				}

				if ((mCurrentDirection == Elevator.Direction.MOVING_DOWN && (mCurrentFloor.getNumber() != mDestination.getNumber() + 1)) || (mCurrentDirection == Elevator.Direction.MOVING_UP && (mCurrentFloor.getNumber() != mDestination.getNumber() - 1))) {
					elevator.scheduleStateChange(Elevator.ElevatorState.MOVING, 2);
				}
				else {

					elevator.scheduleStateChange(Elevator.ElevatorState.DECELERATING, 2);

				}
				return;

			case DECELERATING:
				elevator.setCurrentDirection(mDesiredDirection);
				mCurrentFloor.elevatorDecelerating(elevator);
				elevator.scheduleModeChange(new ActiveMode(), Elevator.ElevatorState.DOORS_OPENING, 3);
				return;

			default:
				return;
		}
	}
}
