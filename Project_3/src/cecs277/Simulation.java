package cecs277;

import cecs277.buildings.Building;
import cecs277.elevators.DispatchMode;
import cecs277.events.SimulationEvent;
import cecs277.events.SpawnPassengerEvent;
import cecs277.logging.Logger;
import cecs277.logging.StandardOutLogger;
import cecs277.passengers.*;

import java.util.*;

public class Simulation {
	private List<PassengerFactory> mPassengerFactories = new ArrayList<>();
	private Random mRandom;
	private PriorityQueue<SimulationEvent> mEvents = new PriorityQueue<>();
	private long mCurrentTime;

	public Building getBuilding() {
		return mBuilding;
	}

	public List<PassengerFactory> getPassengerFactories() {
		return mPassengerFactories;
	}

	private Building mBuilding;
	/**
	 * Seeds the Simulation with a given random number generator.
	 */
	public Simulation(Random random) {
		mRandom = random;
	}
	
	/**
	 * Gets the current time of the simulation.
	 */
	public long currentTime() {
		return mCurrentTime;
	}
	
	/**
	 * Access the Random object for the simulation.
	 */
	public Random getRandom() {
		return mRandom;
	}
	
	/**
	 * Adds the given event to a priority queue sorted on the scheduled time of execution.
	 */
	public void scheduleEvent(SimulationEvent ev) {
		mEvents.add(ev);
	}
	
	public void startSimulation(Scanner input) {
		StandardOutLogger log = new StandardOutLogger(this);
		Logger.setInstance(log);


		System.out.println("Enter number of floors: ");
		int floors = input.nextInt();
		System.out.println("Enter number of elevators: ");
		int elevCount = input.nextInt();

		mBuilding = new Building(floors, elevCount, this);
		SpawnPassengerEvent ev = new SpawnPassengerEvent(0, mBuilding);
		scheduleEvent(ev);
		
		long nextSimLength = -1;
		
		// Set this boolean to true to make the simulation run at "real time".
		boolean simulateRealTime = false;
		// Change the scale below to less than 1 to speed up the "real time".
		double realTimeScale = 1.0;

		System.out.println("Enter time in seconds to simulate: ");
		nextSimLength = input.nextInt();

		while(nextSimLength != -1) {
			long nextStopTime = mCurrentTime + nextSimLength;
			// If the next event in the queue occurs after the requested sim time, then just fast forward to the requested sim time.
			if (mEvents.peek().getScheduledTime() >= nextStopTime) {
				mCurrentTime = nextStopTime;
			}

			// As long as there are events that happen between "now" and the requested sim time, process those events and
			// advance the current time along the way.
			while (!mEvents.isEmpty() && mEvents.peek().getScheduledTime() <= nextStopTime) {
				SimulationEvent nextEvent = mEvents.poll();

				long diffTime = nextEvent.getScheduledTime() - mCurrentTime;
				if (simulateRealTime && diffTime > 0) {
					try {
						Thread.sleep((long) (realTimeScale * diffTime * 1000));
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}

				mCurrentTime += diffTime;
				nextEvent.execute(this);
				Logger.getInstance().logEvent(nextEvent);
			}

			Logger.getInstance().logString("Building");
			Logger.getInstance().logString(mBuilding.toString());

			Logger.getInstance().logString("Enter time in seconds to simulate: ");
			nextSimLength = input.nextInt();
		}
		/*
		 DONE: the simulation stops after one round of simulation. Write a loop that continues to ask the user
		 how many seconds to simulate, simulates that many seconds, and stops only if they choose -1 seconds.
		*/
	}
	
	public static void main(String[] args) {
		Scanner s = new Scanner(System.in);
		System.out.println("Enter seed value: ");
		int x = s.nextInt();

		System.out.println("Do you wish to enable the Disabled Operation?\nType y for yes or n for no: ");
		String disabledOp = s.next();
		if (disabledOp.equals("y")){
			DispatchMode.canBeDisabled = true;
		}

		Simulation sim = new Simulation(new Random(x));
		sim.mPassengerFactories.add(new VisitorPassengerFactory(10));
		sim.mPassengerFactories.add(new WorkerPassengerFactory(2));
		sim.mPassengerFactories.add(new ChildPassengerFactory(3));
		sim.mPassengerFactories.add(new DeliveryPassengerFactory(2));
		sim.mPassengerFactories.add(new StonerPassengerFactory(1));
		sim.mPassengerFactories.add(new JerkPassengerFactory(2));
		sim.startSimulation(s);
	}
}
