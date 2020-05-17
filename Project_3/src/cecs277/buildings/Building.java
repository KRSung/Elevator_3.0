package cecs277.buildings;

import cecs277.Simulation;
import cecs277.elevators.Elevator;
import cecs277.elevators.ElevatorObserver;
import cecs277.passengers.Passenger;

import java.lang.reflect.Array;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.stream.Collectors;

public class Building implements ElevatorObserver, FloorObserver {
	private List<Elevator> mElevators = new ArrayList<>();
	private List<Floor> mFloors = new ArrayList<>();
	private Simulation mSimulation;
	private Queue<FloorRequest> mWaitingFloors = new ArrayDeque<>();
	
	public Building(int floors, int elevatorCount, Simulation sim) {
		mSimulation = sim;
		
		// Construct the floors, and observe each one.
		for (int i = 0; i < floors; i++) {
			Floor f = new Floor(i + 1, this);
			f.addObserver(this);
			mFloors.add(f);
		}
		
		// Construct the elevators, and observe each one.
		for (int i = 0; i < elevatorCount; i++) {
			Elevator elevator = new Elevator(i + 1, this);
			elevator.addObserver(this);
			for (Floor f : mFloors) {
				elevator.addObserver(f);
			}
			mElevators.add(elevator);
		}
	}

	private class FloorRequest {
		private Floor mDestination;
		private Elevator.Direction mDirection;

		private FloorRequest(Floor destination, Elevator.Direction direction) {
			mDestination = destination;
			mDirection = direction;
		}
	}

	public List<Elevator> getElevators(){
		return mElevators;
	}

	// DONE: recreate your toString() here.
	public String toString(){
		StringBuilder visualRepresentation = new StringBuilder();
		for (int i = getFloorCount(); i > 0; i--){

//            adds a padding so the floor numbers line up visually
			if (i < 10){
				visualRepresentation.append("  ").append(i).append(":  |");
			}
			else if(i < 100){
				visualRepresentation.append(" ").append(i).append(":  |");
			}
			else{
				visualRepresentation.append(i).append(":  |");
			}

			for(int j = 0; j < mElevators.size(); j++){
				if(mElevators.get(j).getCurrentFloor().getNumber() == i){
					visualRepresentation.append(" X |");
				}
				else{
					visualRepresentation.append("   |");
				}
			}

			if (mFloors.get(i - 1).getDownButtonPressed() && mFloors.get(i - 1).getUpButtonPressed()){
				visualRepresentation.append(" \u2195\uFE0F");
			}
			else if (mFloors.get(i - 1).getDownButtonPressed()){
				visualRepresentation.append(" \u2B07\uFE0F");
			}
			else if (mFloors.get(i - 1).getUpButtonPressed()){
				visualRepresentation.append(" \u2B06\uFE0F");
			}

			visualRepresentation.append( " " +
				mFloors.get(i - 1).getWaitingPassengers().stream()
					.map(p -> p.getShortName() + p.getId() + "->" + p.getDestination())
					.collect(Collectors.joining(", ")
				)
			);

			visualRepresentation.append("\n");
		}

		for (int i = 0; i < mElevators.size(); i++){
			visualRepresentation.append(mElevators.get(i)).append("\n");
		}
		return visualRepresentation.toString();
	}
	
	public int getFloorCount() {
		return mFloors.size();
	}


	//FIXME index out of bounds for above and below
	//FIXME ex) Index 11 out of bounds for length 11
	//FIXME ex) index -1 out of bounds for length 23
	public Floor getFloor(int floor) {
		return mFloors.get(floor - 1);
	}
	
	public Simulation getSimulation() {
		return mSimulation;
	}
	
	@Override
	public void elevatorDecelerating(Elevator elevator) {
		// Have to implement all interface methods even if we don't use them.
	}
	
	@Override
	public void elevatorDoorsOpened(Elevator elevator) {
		// Don't care.
	}
	
	@Override
	public void elevatorWentIdle(Elevator elevator) {
		if (!mWaitingFloors.isEmpty()){
			FloorRequest temp = mWaitingFloors.remove();
			elevator.dispatchToFloor(temp.mDestination, temp.mDirection);
		}
	}
	
	@Override
	public void elevatorArriving(Floor sender, Elevator elevator) {
		mWaitingFloors.removeIf(f -> f.mDestination.getNumber() == sender.getNumber() &&
				(elevator.getCurrentDirection() == Elevator.Direction.NOT_MOVING ||
						elevator.getCurrentDirection() == f.mDirection));
	}
	
	@Override
	public void directionRequested(Floor floor, Elevator.Direction direction) {
		boolean elevatorDispatched = false;

		for(Elevator e: mElevators){
			if (e.canBeDispatchedToFloor(floor)){
				e.dispatchToFloor(floor, direction);
				elevatorDispatched = true;
				break;
			}
		}

		if (!elevatorDispatched) {
			mWaitingFloors.add(new FloorRequest(floor, direction));
		}
	}
}
