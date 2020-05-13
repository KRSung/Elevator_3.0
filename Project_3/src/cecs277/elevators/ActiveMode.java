package cecs277.elevators;

import cecs277.Simulation;
import cecs277.buildings.Building;
import cecs277.buildings.Floor;

import java.util.ArrayList;

/**
 * An ActiveMode elevator is handling at least one floor request.
 */
public class ActiveMode implements OperationMode {
	private Floor mCurrentFloor;
	private Building mBuilding;
	private boolean[] mRequestedFloors;
	private ArrayList<ElevatorObserver> mObservers;
	private Elevator.Direction mCurrentDirection;
	private Elevator.ElevatorState mCurrentState;

	
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
	public void dispatchToFloor(Elevator elevator, Floor targetFloor, Elevator.Direction targetDirection) { }

	@Override
	public void directionRequested(Elevator elevator, Floor floor, Elevator.Direction direction) { }

	@Override
	public void tick(Elevator elevator) {
		Simulation s = elevator.getBuilding().getSimulation();
		int passengerChangeCount = 0;
		mCurrentState = elevator.getCurrentState();
		mCurrentDirection = elevator.getCurrentDirection();
		mRequestedFloors = elevator.getRequestedFloors();
		mCurrentFloor = elevator.getCurrentFloor();
		mObservers = elevator.getObservers();
		mBuilding = elevator.getBuilding();

		switch (mCurrentState) {


			case DOORS_OPENING:
				elevator.scheduleStateChange(Elevator.ElevatorState.DOORS_OPEN, 2);
//				System.out.println("mWaitingFloors: " + mBuilding.getmWaitingFloors().toString());
//				System.out.printf("Waiting Passengers: ");
//				for (Passenger p : getCurrentFloor().getWaitingPassengers())
//					System.out.printf(" " + p.getDestination());
//				System.out.println();
				return;

			case DOORS_OPEN:
				passengerChangeCount = 0;
//				for (int i = 0; i < mObservers.size(); i++){
				ArrayList<ElevatorObserver> temp = new ArrayList<>(mObservers);
				for (ElevatorObserver o : temp) {
//					if (o instanceof WorkerPassenger && temp.size() + this.getPassengerCount() )
					o.elevatorDoorsOpened(elevator);
				}
				elevator.scheduleStateChange(Elevator.ElevatorState.DOORS_CLOSING, (passengerChangeCount / 2) + 1);
//				System.out.printf("Waiting Passengers: ");
//				for (Passenger p : getCurrentFloor().getWaitingPassengers())
//					System.out.printf(" " + p.getDestination());
//				System.out.println();
//				System.out.println(mBuilding.getmWaitingFloors().toString());
				return;

			case DOORS_CLOSING:
				if (mCurrentDirection == Elevator.Direction.MOVING_DOWN) {
					if (hasRequestedFloorsDown()) {
						elevator.scheduleStateChange(Elevator.ElevatorState.ACCELERATING, 2);
					} else if (hasRequestedFloorsUp()) {
						elevator.setCurrentDirection(Elevator.Direction.MOVING_UP);
						elevator.scheduleStateChange(Elevator.ElevatorState.DOORS_OPENING, 2);
					} else {
						elevator.setCurrentDirection(Elevator.Direction.NOT_MOVING);
						elevator.scheduleModeChange(new IdleMode(), Elevator.ElevatorState.IDLE_STATE, 2);
					}
				} else if (mCurrentDirection == Elevator.Direction.MOVING_UP) {
//					if (mCurrentDirection != Direction.MOVING_UP){
//						System.out.println("\n\n-----------------------------------------------------------------------------------\n\n");
//						System.out.println("ERROR: Case Doors_closing: Expected Direction Moving Up, Received " + mCurrentDirection);
//						System.out.println("\n\n-----------------------------------------------------------------------------------\n\n");
//					}
					if (hasRequestedFloorsUp()) {
						elevator.scheduleStateChange(Elevator.ElevatorState.ACCELERATING, 2);
					} else if (hasRequestedFloorsDown()) {
						elevator.setCurrentDirection(Elevator.Direction.MOVING_DOWN);
						elevator.scheduleStateChange(Elevator.ElevatorState.DOORS_OPENING, 2);
					} else {
						elevator.setCurrentDirection(Elevator.Direction.NOT_MOVING);
						elevator.scheduleModeChange(new IdleMode(), Elevator.ElevatorState.IDLE_STATE, 2);
					}
				} else {
//					elevator.scheduleStateChange(Elevator.ElevatorState.IDLE_STATE, 2);
					elevator.scheduleModeChange(new IdleMode(), Elevator.ElevatorState.IDLE_STATE, 2);
				}

				return;

			case ACCELERATING:
				elevator.getCurrentFloor().removeObserver(elevator);
				elevator.scheduleStateChange(Elevator.ElevatorState.MOVING, 3);

				return;

			case MOVING:
				if (mCurrentDirection == Elevator.Direction.MOVING_UP) {
					elevator.setCurrentFloor(mBuilding.getFloor(mCurrentFloor.getNumber() + 1));
//					if (mCurrentFloor.getNumber() == mBuilding.getFloorCount() - 1) {
//						scheduleStateChange(ElevatorState.DECELERATING, 2);
//					}
//					else
					if (mRequestedFloors[mCurrentFloor.getNumber()] ||
							mCurrentFloor.directionIsPressed(Elevator.Direction.MOVING_UP) ||
							mCurrentFloor.getNumber() == mBuilding.getFloorCount()) {
						elevator.scheduleStateChange(Elevator.ElevatorState.DECELERATING, 2);
					} else {
						elevator.scheduleStateChange(Elevator.ElevatorState.MOVING, 2);
					}
				} else /*if (mCurrentDirection == Direction.MOVING_DOWN) */ {
//					if (mCurrentDirection != Direction.MOVING_DOWN){
//						System.out.println("\n\n-----------------------------------------------------------------------------------\n\n");
//						System.out.println("ERROR: Case moving: Expected Direction Moving Down, Received " + mCurrentDirection);
//						System.out.println("\n\n-----------------------------------------------------------------------------------\n\n");
//					}
//					if (mCurrentFloor.getNumber() == 1) {
//					scheduleStateChange(ElevatorState.DECELERATING, 2);
//					return;
//					}
					elevator.setCurrentFloor(mBuilding.getFloor(mCurrentFloor.getNumber() - 1));
//					if (mCurrentFloor.getNumber() == 1) {
//						scheduleStateChange(ElevatorState.DECELERATING, 2);
//					}
//					else
					if (mRequestedFloors[mCurrentFloor.getNumber() - 1] ||
							mCurrentFloor.directionIsPressed(Elevator.Direction.MOVING_DOWN) ||
							mCurrentFloor.getNumber() == 1) {
						elevator.scheduleStateChange(Elevator.ElevatorState.DECELERATING, 2);
					} else {
						elevator.scheduleStateChange(Elevator.ElevatorState.MOVING, 2);
					}
				}
				return;

			case DECELERATING:
				mRequestedFloors[mCurrentFloor.getNumber() - 1] = false;
				if (mCurrentDirection == Elevator.Direction.MOVING_UP) {
//					mCurrentFloor.clearDirection(Direction.MOVING_UP);
					if (mCurrentFloor.directionIsPressed(Elevator.Direction.MOVING_UP) || hasRequestedFloorsUp()) {
						System.out.printf("");
					} else if (mCurrentFloor.directionIsPressed(Elevator.Direction.MOVING_DOWN)) {
						elevator.setCurrentDirection(Elevator.Direction.MOVING_DOWN);
					}
//							&& mCurrentFloor.directionIsPressed(Direction.MOVING_DOWN) ){
//						mCurrentFloor.elevatorDecelerating(this);
//						scheduleStateChange(ElevatorState.DOORS_OPENING, 3);
//						mCurrentDirection = Direction.MOVING_DOWN;
//					}
					else {
						elevator.setCurrentDirection(Elevator.Direction.NOT_MOVING);
					}
				} else /*if (mCurrentDirection == Direction.MOVING_DOWN )*/ {
//					if (mCurrentDirection != Direction.MOVING_DOWN){
//						System.out.println("\n\n-----------------------------------------------------------------------------------\n\n");
//						System.out.println("ERROR: Case Decelerating: Expected Direction Moving Down, Received " + mCurrentDirection);
//						System.out.println("\n\n-----------------------------------------------------------------------------------\n\n");
//					}
					if (mCurrentFloor.directionIsPressed(Elevator.Direction.MOVING_DOWN) || hasRequestedFloorsDown()) {
						System.out.printf("");
					} else if (mCurrentFloor.directionIsPressed(Elevator.Direction.MOVING_UP)) {
						elevator.setCurrentDirection(Elevator.Direction.MOVING_UP);
					}
//							&& mCurrentFloor.directionIsPressed(Direction.MOVING_UP) ){
//						mCurrentFloor.elevatorDecelerating(this);
//						scheduleStateChange(ElevatorState.DOORS_OPENING, 3);
//						mCurrentDirection = Direction.MOVING_UP;
//					}
					else {
						elevator.setCurrentDirection(Elevator.Direction.NOT_MOVING);
					}
				}
//				else {
//					scheduleStateChange(ElevatorState.DOORS_OPENING, 3);
//				}
				mCurrentFloor.elevatorDecelerating(elevator);
				elevator.scheduleStateChange(Elevator.ElevatorState.DOORS_OPENING, 3);
				return;

			default:
				return;
		}
	}

	private boolean hasRequestedFloorsUp(){
		for(int i = mCurrentFloor.getNumber(); i < mBuilding.getFloorCount(); i++){
			if (mRequestedFloors[i]) {
				return true;
			}
		}
		return false;
	}

	private boolean hasRequestedFloorsDown(){
		for(int i = mCurrentFloor.getNumber() - 2; i >= 0; i--){
			if (mRequestedFloors[i]) {
				return true;
			}
		}
		return false;
	}
}
