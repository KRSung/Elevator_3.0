package cecs277.passengers;

import cecs277.buildings.Floor;
import cecs277.buildings.FloorObserver;
import cecs277.elevators.Elevator;
import cecs277.elevators.ElevatorObserver;

/**
 * A passenger that is either waiting on a floor or riding an elevator.
 */
public class Passenger implements FloorObserver, ElevatorObserver {
	private int mIdentifier;
	private PassengerState mCurrentState;
	private String mName, mShortName;
	private TravelStrategy mTravelStrategy;
	private BoardingStrategy mBoardingStrategy;
	private EmbarkingStrategy mEmbarkingStrategy;
	private DebarkingStrategy mDebarkingStrategy;

	public Passenger(String name, String shortName,
					 TravelStrategy travelStrategy,
					 BoardingStrategy boardingStrategy,
					 EmbarkingStrategy embarkingStrategy,
					 DebarkingStrategy debarkingStrategy) {

		mName = name;
		mShortName = shortName;
		mTravelStrategy = travelStrategy;
		mBoardingStrategy = boardingStrategy;
		mEmbarkingStrategy = embarkingStrategy;
		mDebarkingStrategy = debarkingStrategy;
		mIdentifier = nextPassengerId();
		mCurrentState = PassengerState.WAITING_ON_FLOOR;
	}

	// An enum for determining whether a Passenger is on a floor, an elevator, or busy (visiting a room in the building).
	public enum PassengerState {
		WAITING_ON_FLOOR,
		ON_ELEVATOR,
		BUSY
	}
	
	// A cute trick for assigning unique IDs to each object that is created. (See the constructor.)
	private static int mNextId;

	protected static int nextPassengerId() {
		return ++mNextId;
	}
	
	public void setState(PassengerState state) {
		mCurrentState = state;
	}

	public String getName() {
		return mName;
	}

	public String getShortName (){
		return mShortName;
	}

	public int getDestination(){
		return mTravelStrategy.getDestination();
	}

	public void scheduleEvent(Floor floor){
		this.mTravelStrategy.scheduleNextDestination(this, floor);
	}

	/**
	 * Gets the passenger's unique identifier.
	 */
	public int getId() {
		return mIdentifier;
	}
	
	
	/**
	 * Handles an elevator arriving at the passenger's current floor.
	 */
	@Override
	public void elevatorArriving(Floor floor, Elevator elevator) {
		// This is a sanity check. A Passenger should never be observing a Floor they are not waiting on.
		if (floor.getWaitingPassengers().contains(this) && mCurrentState == PassengerState.WAITING_ON_FLOOR) {
			Elevator.Direction elevatorDirection = elevator.getCurrentDirection();

			// DONE: check if the elevator is either NOT_MOVING, or is going in the direction that this passenger wants.
			// If so, this passenger becomes an observer of the elevator.
			//FIXME this is a really nasty if statement but i think it does the job
			if (elevator.getCurrentDirection() == Elevator.Direction.NOT_MOVING ||
					((elevator.getCurrentDirection() == Elevator.Direction.MOVING_UP) &&
							(getDestination() > elevator.getCurrentFloor().getNumber())) ||
					((elevator.getCurrentDirection() == Elevator.Direction.MOVING_DOWN) &&
							(getDestination() < elevator.getCurrentFloor().getNumber()))){
				elevator.addObserver(this);
			} else {
				elevator.removeObserver(this);
			}

		}
		// This else should not happen if your code is correct. Do not remove this branch; it reveals errors in your code.
		else {
			throw new RuntimeException(this.getName() + " " + this.getId() + " is observing Floor " + floor.getNumber() + " but they are " +
			 "not waiting on that floor.");
		}
	}

	@Override
	public void elevatorDecelerating(Elevator sender) {

	}

	/**
	 * Handles an observed elevator opening its doors. Depart the elevator if we are on it; otherwise, enter the elevator.
	 */
	@Override
	public void elevatorDoorsOpened(Elevator elevator) {

		if (mCurrentState == PassengerState.ON_ELEVATOR) {
			if (mDebarkingStrategy.willLeaveElevator(this, elevator)) {
				elevator.removePassenger(this);
				elevator.removeObserver(this);
				setState(PassengerState.BUSY);
				mDebarkingStrategy.departedElevator(this, elevator);
			}
		}
		else if (mCurrentState == PassengerState.WAITING_ON_FLOOR){
			if (mBoardingStrategy.willBoardElevator(this, elevator)){
				//TODO after adding the passenger to the elevator inform the embarking strategy so it can request floors
				Floor currentFloor = elevator.getCurrentFloor();
				currentFloor.removeWaitingPassenger(this);
				currentFloor.removeObserver(this);
				for (Elevator e : elevator.getBuilding().getElevators()){
					e.removeObserver(this);
				}
				elevator.addPassenger(this);
				elevator.addObserver(this);
				setState(PassengerState.ON_ELEVATOR);
				this.mEmbarkingStrategy.enteredElevator(this, elevator);
			}
			else {
				elevator.removeObserver(this);
				elevator.getCurrentFloor().requestDirection(elevator.getCurrentDirection());
			}
		}
	}

	// This will be overridden by derived types.
	@Override
	public String toString() {
		return getName() + " " + getId();
	}
	
	@Override
	public void directionRequested(Floor sender, Elevator.Direction direction) {
		// Don't care.
	}
	
	@Override
	public void elevatorWentIdle(Elevator elevator) {
		// Don't care about this.
	}
	
	// The next two methods allow Passengers to be used in data structures, using their id for equality. Don't change 'em.
	@Override
	public int hashCode() {
		return Integer.hashCode(mIdentifier);
	}
	
	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Passenger passenger = (Passenger)o;
		return mIdentifier == passenger.mIdentifier;
	}

}