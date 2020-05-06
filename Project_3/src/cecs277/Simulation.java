package cecs277;

import cecs277.buildings.Building;
import cecs277.events.SimulationEvent;
import cecs277.events.SpawnPassengerEvent;
import cecs277.passengers.PassengerFactory;
import cecs277.passengers.VisitorFactory;
import cecs277.passengers.WorkerFactory;
import com.sun.security.jgss.GSSUtil;

import java.util.List;
import java.util.PriorityQueue;
import java.util.Random;
import java.util.Scanner;

public class Simulation {
	private int mTotal;
	private List<PassengerFactory> mPassengerFactories;
	private Random mRandom;
	private PriorityQueue<SimulationEvent> mEvents = new PriorityQueue<>();
	private long mCurrentTime;

	public Building getBuilding() {
		return mBuilding;
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
		System.out.println("Create Worker Factory (y = yes, n = no): ");
		if (input.next().equals("y")){
			System.out.println("Enter weight for factory: ");
			mPassengerFactories.add(new WorkerFactory(this, input.nextInt()));
		}

		System.out.println("Create Visitor Factory (y = yes, n = no): ");
		if (input.next().equals("y")){
			System.out.println("Enter weight for factory: ");
			mPassengerFactories.add(new VisitorFactory(this, input.nextInt()));
		}

		System.out.println("Create Child Factory (y = yes, n = no): ");
		if (input.next().equals("y")){
			System.out.println("Enter weight for factory: ");
			mPassengerFactories.add(new ChildFactory(this, input.nextInt()));
		}

		System.out.println("Create Delivery Person Factory (y = yes, n = no): ");
		if (input.next().equals("y")){
			System.out.println("Enter weight for factory: ");
			mPassengerFactories.add(new DeliveryPersonFactory(this, input.nextInt()));
		}

		System.out.println("Create Stoner Factory (y = yes, n = no): ");
		if (input.next().equals("y")){
			System.out.println("Enter weight for factory: ");
			mPassengerFactories.add(new StonerFactory(this, input.nextInt()));
		}

		System.out.println("Create Jerk Factory (y = yes, n = no): ");
		if (input.next().equals("y")){
			System.out.println("Enter weight for factory: ");
			mPassengerFactories.add(new JerkFactory(this, input.nextInt()));
		}

		System.out.println("Enter number of floors: ");
		int floors = input.nextInt();
		System.out.println("Enter number of elevators: ");
		int elevCount = input.nextInt();

//		for(PassengerFactory p: mPassengerFactories){
//			System.out.println("Enter the weight of the " + p + " factory:");
//			input.nextInt();
//		}

		mBuilding = new Building(floors, elevCount, this);
		SpawnPassengerEvent ev = new SpawnPassengerEvent(0, mBuilding);
		scheduleEvent(ev);
		
		long nextSimLength = -1;
		
		// Set this boolean to true to make the simulation run at "real time".
		boolean simulateRealTime = false;
		// Change the scale below to less than 1 to speed up the "real time".
		double realTimeScale = 1.0;
		
		// DONE: the simulation currently stops at 200s. Instead, ask the user how long they want to simulate.
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
				System.out.println(nextEvent);
			}

			// DONE: print the Building after simulating the requested time.

			System.out.println("Building");
			mBuilding.toString();

			//i didnt want to check with an if statement each iteration
			//if you prefer that way or have a better way we can change it
			System.out.println("Enter time in seconds to simulate: ");
			nextSimLength = input.nextInt();
		}
		/*
		 DONE: the simulation stops after one round of simulation. Write a loop that continues to ask the user
		 how many seconds to simulate, simulates that many seconds, and stops only if they choose -1 seconds.
		*/
	}
	
	public static void main(String[] args) {
		Scanner s = new Scanner(System.in);
		// DONE: ask the user for a seed value and change the line below.
		System.out.println("Enter seed value: ");
		int x = s.nextInt();
		Simulation sim = new Simulation(new Random(x));
		sim.startSimulation(s);
	}
}
