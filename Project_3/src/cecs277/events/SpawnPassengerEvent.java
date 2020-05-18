package cecs277.events;

import cecs277.Simulation;
import cecs277.buildings.Building;
import cecs277.passengers.Passenger;
import cecs277.passengers.PassengerFactory;

import java.util.stream.StreamSupport;

/**
 * A simulation event that adds a new random passenger on floor 1, and then schedules the next spawn event.
 */
public class SpawnPassengerEvent extends SimulationEvent {

	// After executing, will reference the Passenger object that was spawned.
	private Passenger mPassenger;
	private Building mBuilding;
	
	public SpawnPassengerEvent(long scheduledTime, Building building) {
		super(scheduledTime);
		mBuilding = building;
		setPriority(4);
	}
	
	@Override
	public String toString() {
		return super.toString() + "Adding " + mPassenger.getName() + " " + mPassenger.getId()
				+ " [-> " + mPassenger.getDestination() + "] to floor 1.";
	}
	
	@Override
	public void execute(Simulation sim) {
		Iterable<PassengerFactory> factories = sim.getPassengerFactories();
		int totalWeight = StreamSupport.stream(factories.spliterator(), false)
				.mapToInt(f -> f.factoryWeight()).sum();

		int num = 0;
		int r = sim.getRandom().nextInt(totalWeight);
		for(PassengerFactory f: factories){
			num += f.factoryWeight();
			if (r < num){
				mPassenger = new Passenger(f.factoryName(),
						f.shortName(),
						f.createTravelStrategy(sim),
						f.createBoardingStrategy(sim),
						f.createEmbarkingStrategy(sim),
						f.createDebarkingStrategy(sim));
				break;
			}
		}

		mBuilding.getFloor(1).addWaitingPassenger(mPassenger);
		long increaseTime = 1 + sim.getRandom().nextInt(30) + sim.currentTime();
		SpawnPassengerEvent next = new SpawnPassengerEvent(increaseTime, mBuilding);
		sim.scheduleEvent(next);
	}
}
