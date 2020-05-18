package cecs277.events;

import cecs277.Simulation;
import cecs277.elevators.Elevator;
import cecs277.passengers.Passenger;

/**
 * Represents an event that occurs in the simulation, which acts to update the state of the simulation in some way.
 * Derived types code the "update" logic by overriding the execute method.
 */
public abstract class SimulationEvent implements Comparable<SimulationEvent> {
	// The time at which this event is scheduled to be executed.
	private long mScheduledTime;

	public void setPriority(Integer priority) {
		mPriority = priority;
	}

	private Integer mPriority;
	
	/**
	 * Sets the scheduled time of the event.
	 */
	public SimulationEvent(long scheduledTime) {
		mScheduledTime = scheduledTime;
	}
	
	/**
	 * Gets the time at which this event should be executed. The start of the simulation is at time 0. Each unit of time
	 * is equal to 1 second.
	 */
	public long getScheduledTime() {
		return mScheduledTime;
	}
	
	/**
	 *
	 * @param sim
	 */
	public abstract void execute(Simulation sim);
	
	/**
	 * Used for sorting a priority queue, with the smallest scheduled time coming out first.
	 */
	@Override
	public int compareTo(SimulationEvent o) {
//		if(mScheduledTime != o.mScheduledTime){
//			return Long.compare(mScheduledTime, o.mScheduledTime);
//		}
//		else if (this instanceof ElevatorModeEvent){
//			if(!(o instanceof ElevatorModeEvent)){
//				return 1;
//			}
//			else{
//				return 0;
//			}
//		}
//		else if (this instanceof ElevatorStateEvent){
//			if(o instanceof ElevatorStateEvent){
//				return 0;
//			}
//			else if (o instanceof ElevatorModeEvent){
//				return -1;
//			}
//			else{
//				return 1;
//			}
//		}
//		else if (this instanceof PassengerNextDestinationEvent){
//			if(o instanceof PassengerNextDestinationEvent){
//				return 0;
//			}
//			else if (o instanceof ElevatorModeEvent || o instanceof ElevatorStateEvent){
//				return -1;
//			}
//			else{
//				return 1;
//			}
//		}
//		else if (this instanceof SpawnPassengerEvent){
//			if(o instanceof SpawnPassengerEvent){
//				return 0;
//			}
//			else {
//				return -1;
//			}
//		}

		if (mScheduledTime != o.mScheduledTime || mScheduledTime == 0){
			return Long.compare(mScheduledTime, o.mScheduledTime);

		}
		return Integer.compare(mPriority, o.mPriority);

	}

	@Override
	public String toString() {
		return "";
	}
}
