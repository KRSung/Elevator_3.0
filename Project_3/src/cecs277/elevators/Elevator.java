package cecs277.elevators;

import cecs277.Simulation;
import cecs277.buildings.Building;
import cecs277.buildings.Floor;
import cecs277.buildings.FloorObserver;
import cecs277.events.ElevatorDisabledEvent;
import cecs277.events.ElevatorModeEvent;
import cecs277.events.ElevatorStateEvent;
import cecs277.passengers.Passenger;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
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

	public void disable(DispatchMode dispatchMode){
		scheduleModeChange(new DisabledMode(), Elevator.ElevatorState.IDLE_STATE, 0);
		scheduleDisabledMode(300, dispatchMode);
	}

	public void enable(DispatchMode dispatchMode){
		scheduleModeChange(dispatchMode, Elevator.ElevatorState.IDLE_STATE, 0);
	}
	
	/**
	 * Helper method to schedule a state change in a given number of seconds from now.
	 */
	protected void scheduleStateChange(ElevatorState state, long timeFromNow) {
		Simulation sim = mBuilding.getSimulation();
		sim.scheduleEvent(new ElevatorStateEvent(timeFromNow + sim.currentTime(), state, this));
	}

	protected void scheduleModeChange(OperationMode operationMode, Elevator.ElevatorState state, long timeFromNow){
		Simulation sim = mBuilding.getSimulation();
		sim.scheduleEvent(new ElevatorModeEvent(timeFromNow + sim.currentTime(), operationMode, state,
				this));
	}

	protected void scheduleDisabledMode(long timeFromNow, DispatchMode dispatchMode){
		Simulation sim = mBuilding.getSimulation();
		sim.scheduleEvent(new ElevatorDisabledEvent(timeFromNow + sim.currentTime(), this, dispatchMode));
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
		passengerChangeCount = 0;
	}

	public boolean canBeDispatchedToFloor(Floor floor){
		return mOperationMode.canBeDispatchedToFloor(this, floor);
	}

	/**
	 * Sends an idle elevator to the given floor.
	 */
	public void dispatchToFloor(Floor floor) {
		if (floor.getWaitingPassengers().get(0).getDestination() > floor.getNumber()){
			mOperationMode.dispatchToFloor(this, floor, Direction.MOVING_UP);
		}
		else {
			mOperationMode.dispatchToFloor(this, floor, Direction.MOVING_DOWN);
		}
	}
	
	// Simple accessors
	public int getPassengerChangeCount(){
		return passengerChangeCount;
	}

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

	public int getNumber() {
		return mNumber;
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
		String buttons = "";
		for (int i = 0; i < mRequestedFloors.length; i++){
			if (mRequestedFloors[i]){
				buttons += i + 1 + ", ";
			}
		}
		if (buttons.length() > 1){
			buttons = buttons.substring(0, buttons.length() - 2);
		}
//		buttons = buttons.substring(0, buttons.length());
		return "Elevator " +  mNumber + " [" + mOperationMode + "] - " + mCurrentFloor + " - " + mCurrentState + " - " + mCurrentDirection + " - "
		 + "[" +
			mPassengers.stream()
				.map(p -> p.getShortName() + p.getId())
				.collect(Collectors.joining(", ")
		) + "] {" +
			buttons
		+ "}";
	}
	
}