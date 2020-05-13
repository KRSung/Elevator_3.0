package cecs277.elevators;

import cecs277.Simulation;
import cecs277.buildings.Building;
import cecs277.buildings.Floor;
import cecs277.buildings.FloorObserver;
import cecs277.events.ElevatorModeEvent;
import cecs277.events.ElevatorStateEvent;
import cecs277.passengers.Passenger;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Elevator implements FloorObserver {
	private OperationMode mOperationMode = new IdleMode();

	public void setOpMode(OperationMode opMode) {
		mOperationMode = opMode;
	}

	public enum ElevatorState {
		IDLE_STATE,
		DOORS_OPENING,
		DOORS_CLOSING,
		DOORS_OPEN,
		ACCELERATING,
		DECELERATING,
		MOVING
	}
	
	public enum Direction {
		NOT_MOVING,
		MOVING_UP,
		MOVING_DOWN
	}

	private int mNumber;
	private Building mBuilding;
	private ElevatorState mCurrentState = ElevatorState.IDLE_STATE;
	private Direction mCurrentDirection = Direction.NOT_MOVING;
	private Floor mCurrentFloor;
	private int passengerChangeCount = 0;
	private List<Passenger> mPassengers = new ArrayList<>();
	private ArrayList<ElevatorObserver> mObservers = new ArrayList<>();
	
	// Done: declare a field to keep track of which floors have been requested by passengers.
	private boolean mRequestedFloors[];
	
	
	public Elevator(int number, Building bld) {
		mNumber = number;
		mBuilding = bld;
		mCurrentFloor = bld.getFloor(1);
		
		scheduleStateChange(ElevatorState.IDLE_STATE, 0);
		mRequestedFloors = new boolean[mBuilding.getFloorCount()];
	}
	
	/**
	 * Helper method to schedule a state change in a given number of seconds from now.
	 */
	protected void scheduleStateChange(ElevatorState state, long timeFromNow) {
		Simulation sim = mBuilding.getSimulation();
		sim.scheduleEvent(new ElevatorStateEvent(timeFromNow + sim.currentTime(), state, this));
	}

	protected void scheduleModeChange(OperationMode operationMode, Elevator.ElevatorState state, long timeFromNow){
		mCurrentState = state;
		Simulation sim = mBuilding.getSimulation();
		sim.scheduleEvent(new ElevatorModeEvent(timeFromNow + sim.currentTime(), operationMode,
				this));
	}

	protected void announceElevatorDecelerating(){
		ArrayList<ElevatorObserver> temp = new ArrayList<>(mObservers);
		for(ElevatorObserver o : temp){
			o.elevatorDecelerating(this);
		}
	}
	
	/**
	 * Adds the given passenger to the elevator's list of passengers, and requests the passenger's destination floor.
	 */
	public void addPassenger(Passenger passenger) {
		// Done: add the passenger's destination to the set of requested floors.
		mPassengers.add(passenger);
		mRequestedFloors[passenger.getDestination() - 1] = true;
		passengerChangeCount++;
	}

	public void removePassenger(Passenger passenger) {
		mPassengers.remove(passenger);
		passengerChangeCount++;
	}

	public void announceElevatorIdle(){
		for (ElevatorObserver o : mObservers) {
			o.elevatorWentIdle(this);
		}
	}
	
	
	/**
	 * Schedules the elevator's next state change based on its current state.
	 */
	public void tick() {
		mOperationMode.tick(this);

//		// TODO: port the logic of your state changes from Project 1, accounting for the adjustments in the spec.
//		// TODO: State changes are no longer immediate; they are scheduled using scheduleStateChange().
//		Simulation s = this.getBuilding().getSimulation();
//		switch (mCurrentState) {
//			case IDLE_STATE:
//				mCurrentFloor.addObserver(this);
//
//				for (ElevatorObserver o : mObservers) {
//					o.elevatorWentIdle(this);
//				}
////				System.out.println(mBuilding.getmWaitingFloors().toString());
//
//				return;
//
//			case DOORS_OPENING:
//				scheduleStateChange(ElevatorState.DOORS_OPEN,2);
////				System.out.println("mWaitingFloors: " + mBuilding.getmWaitingFloors().toString());
////				System.out.printf("Waiting Passengers: ");
////				for (Passenger p : getCurrentFloor().getWaitingPassengers())
////					System.out.printf(" " + p.getDestination());
////				System.out.println();
//				return;
//
//			case DOORS_OPEN:
//				passengerChangeCount = 0;
////				for (int i = 0; i < mObservers.size(); i++){
//				ArrayList<ElevatorObserver> temp = new ArrayList<>(mObservers);
//				for (ElevatorObserver o : temp){
////					if (o instanceof WorkerPassenger && temp.size() + this.getPassengerCount() )
//					o.elevatorDoorsOpened(this);
//				}
//				scheduleStateChange(ElevatorState.DOORS_CLOSING,(passengerChangeCount / 2) + 1);
////				System.out.printf("Waiting Passengers: ");
////				for (Passenger p : getCurrentFloor().getWaitingPassengers())
////					System.out.printf(" " + p.getDestination());
////				System.out.println();
////				System.out.println(mBuilding.getmWaitingFloors().toString());
//				return;
//
//			case DOORS_CLOSING:
//				if (mCurrentDirection == Direction.MOVING_DOWN ) {
//					if (hasRequestedFloorsDown()){
//						scheduleStateChange(ElevatorState.ACCELERATING,2);
//					}
//					else if (hasRequestedFloorsUp()) {
//						mCurrentDirection = Direction.MOVING_UP;
//						scheduleStateChange(ElevatorState.DOORS_OPENING,2);
//					}
//					else{
//						mCurrentDirection = Direction.NOT_MOVING;
//						scheduleStateChange(ElevatorState.IDLE_STATE,2);
//					}
//				}
//				else if (mCurrentDirection == Direction.MOVING_UP) {
////					if (mCurrentDirection != Direction.MOVING_UP){
////						System.out.println("\n\n-----------------------------------------------------------------------------------\n\n");
////						System.out.println("ERROR: Case Doors_closing: Expected Direction Moving Up, Received " + mCurrentDirection);
////						System.out.println("\n\n-----------------------------------------------------------------------------------\n\n");
////					}
//					if (hasRequestedFloorsUp()){
//						scheduleStateChange(ElevatorState.ACCELERATING,2);
//					}
//					else if (hasRequestedFloorsDown()) {
//						mCurrentDirection = Direction.MOVING_DOWN;
//						scheduleStateChange(ElevatorState.DOORS_OPENING,2);
//					}
//					else{
//						mCurrentDirection = Direction.NOT_MOVING;
//						scheduleStateChange(ElevatorState.IDLE_STATE,2);
//					}
//				}
//				else {
//					scheduleStateChange(ElevatorState.IDLE_STATE,2);
//				}
//
//				return;
//
//			case ACCELERATING:
//				getCurrentFloor().removeObserver(this);
//				scheduleStateChange(ElevatorState.MOVING,3);
//
//				return;
//
//			case MOVING:
//				if (mCurrentDirection == Direction.MOVING_UP) {
//					mCurrentFloor = mBuilding.getFloor(mCurrentFloor.getNumber() + 1);
////					if (mCurrentFloor.getNumber() == mBuilding.getFloorCount() - 1) {
////						scheduleStateChange(ElevatorState.DECELERATING, 2);
////					}
////					else
//					if (mRequestedFloors[mCurrentFloor.getNumber() - 1] ||
//						mCurrentFloor.directionIsPressed(Direction.MOVING_UP) ||
//						mCurrentFloor.getNumber() == mBuilding.getFloorCount()) {
//					scheduleStateChange(ElevatorState.DECELERATING,2);
//					}
//					else{
//						scheduleStateChange(ElevatorState.MOVING,2);
//					}
//				}
//				else /*if (mCurrentDirection == Direction.MOVING_DOWN) */{
////					if (mCurrentDirection != Direction.MOVING_DOWN){
////						System.out.println("\n\n-----------------------------------------------------------------------------------\n\n");
////						System.out.println("ERROR: Case moving: Expected Direction Moving Down, Received " + mCurrentDirection);
////						System.out.println("\n\n-----------------------------------------------------------------------------------\n\n");
////					}
////					if (mCurrentFloor.getNumber() == 1) {
////					scheduleStateChange(ElevatorState.DECELERATING, 2);
////					return;
////					}
//					mCurrentFloor = mBuilding.getFloor(mCurrentFloor.getNumber() - 1);
////					if (mCurrentFloor.getNumber() == 1) {
////						scheduleStateChange(ElevatorState.DECELERATING, 2);
////					}
////					else
//					if (mRequestedFloors[mCurrentFloor.getNumber() - 1] ||
//		 					mCurrentFloor.directionIsPressed(Direction.MOVING_DOWN) ||
//							mCurrentFloor.getNumber() == 1) {
//						scheduleStateChange(ElevatorState.DECELERATING, 2);
//					} else {
//						scheduleStateChange(ElevatorState.MOVING, 2);
//					}
//				}
//				return;
//
//			case DECELERATING:
//				mRequestedFloors[mCurrentFloor.getNumber() - 1] = false;
//				if ( mCurrentDirection == Direction.MOVING_UP ) {
////					mCurrentFloor.clearDirection(Direction.MOVING_UP);
//					if (mCurrentFloor.directionIsPressed(Direction.MOVING_UP) || hasRequestedFloorsUp()) {
//						System.out.printf("");
//					} else if (mCurrentFloor.directionIsPressed(Direction.MOVING_DOWN)) {
//						mCurrentDirection = Direction.MOVING_DOWN;
//					}
////							&& mCurrentFloor.directionIsPressed(Direction.MOVING_DOWN) ){
////						mCurrentFloor.elevatorDecelerating(this);
////						scheduleStateChange(ElevatorState.DOORS_OPENING, 3);
////						mCurrentDirection = Direction.MOVING_DOWN;
////					}
//					else {
//						mCurrentDirection = Direction.NOT_MOVING;
//					}
//				}
//				else /*if (mCurrentDirection == Direction.MOVING_DOWN )*/{
////					if (mCurrentDirection != Direction.MOVING_DOWN){
////						System.out.println("\n\n-----------------------------------------------------------------------------------\n\n");
////						System.out.println("ERROR: Case Decelerating: Expected Direction Moving Down, Received " + mCurrentDirection);
////						System.out.println("\n\n-----------------------------------------------------------------------------------\n\n");
////					}
//					if (mCurrentFloor.directionIsPressed(Direction.MOVING_DOWN) || hasRequestedFloorsDown()) {
//						System.out.printf("");
//					} else if (mCurrentFloor.directionIsPressed(Direction.MOVING_UP)) {
//						mCurrentDirection = Direction.MOVING_UP;
//					}
////							&& mCurrentFloor.directionIsPressed(Direction.MOVING_UP) ){
////						mCurrentFloor.elevatorDecelerating(this);
////						scheduleStateChange(ElevatorState.DOORS_OPENING, 3);
////						mCurrentDirection = Direction.MOVING_UP;
////					}
//					else {
//						mCurrentDirection = Direction.NOT_MOVING;
//					}
//				}
////				else {
////					scheduleStateChange(ElevatorState.DOORS_OPENING, 3);
////				}
//				mCurrentFloor.elevatorDecelerating(this);
//				scheduleStateChange(ElevatorState.DOORS_OPENING, 3);
//				return;
//
//			default:
//				return;
//		}
	}

	public boolean canBeDispatchedToFloor(Floor floor){
		return mOperationMode.canBeDispatchedToFloor(this, floor);
	}

	/**
	 * Sends an idle elevator to the given floor.
	 */
	public void dispatchToFloor(Floor floor) {
		if (floor.getNumber() > mCurrentFloor.getNumber()){
			mOperationMode.dispatchToFloor(this, floor, Direction.MOVING_UP);
		}
		else{
			mOperationMode.dispatchToFloor(this, floor, Direction.MOVING_DOWN);
		}
	}
	
	// Simple accessors
	public ArrayList<ElevatorObserver> getObservers() {
		return mObservers;
	}

	public boolean[] getRequestedFloors() {
		return mRequestedFloors;
	}

	public Floor getCurrentFloor() {
		return mCurrentFloor;
	}
	
	public Building getBuilding() {
		return mBuilding;
	}

	public Direction getCurrentDirection() {
		return mCurrentDirection;
	}
	
	/**
	 * Returns true if this elevator is in the idle state.
	 */
	public boolean isIdle() {
		if (mCurrentState == ElevatorState.IDLE_STATE){
			return true;
		}
		return false;
	}
	
	// All elevators have a capacity of 10, for now.
	public int getCapacity() {
		return 10;
	}
	
	public int getPassengerCount() {
		return mPassengers.size();
	}
	
	// Simple mutators
	public void setState(ElevatorState newState) {
		mCurrentState = newState;
	}

	public void setCurrentFloor(Floor currentFloor){
		mCurrentFloor = currentFloor;
	}

	public ElevatorState getCurrentState(){
		return mCurrentState;
	}

	public void setCurrentDirection(Elevator.Direction currentDirection){
		mCurrentDirection = currentDirection;
	}
	
	// Observers
	public void addObserver(ElevatorObserver observer) {
		mObservers.add(observer);
	}
	
	public void removeObserver(ElevatorObserver observer) {
		mObservers.remove(observer);
	}

	public void requestFloor(int floor){
		mRequestedFloors[floor - 1] = true;
	}
	
	
	// FloorObserver methods
	@Override
	public void elevatorArriving(Floor floor, Elevator elevator) {
		// Not used.
	}
	
	/**
	 * Triggered when our current floor receives a direction request.
	 */
	@Override
	public void directionRequested(Floor sender, Direction direction) {
		mOperationMode.directionRequested(this, sender, direction);
	}

	// Voodoo magic.
	@Override
	public String toString() {
		return "Elevator " +  mNumber + " [" + mOperationMode + "] - " + mCurrentFloor + " - " + mCurrentState + " - " + mCurrentDirection + " - "
		 + "[" +
			mPassengers.stream()
				.map(p -> p.getShortName() + p.getId())
				.collect(Collectors.joining(", ")
		) + "] {" +
			mPassengers.stream()
				.map(p -> Integer.toString(p.getDestination()))
				.collect(Collectors.joining(", ")
			)
		+ "}";
	}
	
}