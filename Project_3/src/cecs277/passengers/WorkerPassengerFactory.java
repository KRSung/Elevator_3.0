package cecs277.passengers;

import cecs277.Simulation;
import cecs277.buildings.Building;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class WorkerPassengerFactory implements PassengerFactory {
    public int mFactoryWeight;
    public WorkerPassengerFactory(Simulation simulation, int factoryWeight){
        Passenger p = new Passenger("Worker", "W", createTravelStrategy(simulation),
                createBoardingStrategy(simulation),
                createEmbarkingStrategy(simulation),
                createDebarkingStrategy(simulation));
    }

    @Override
    public String factoryName() {
        return null;
    }

    @Override
    public String shortName() {
        return null;
    }

    public void setFactoryWeight(int factoryWeight){
        mFactoryWeight = factoryWeight;
    }

    @Override
    public int factoryWeight() {
        return mFactoryWeight;
    }

    @Override
    public BoardingStrategy createBoardingStrategy(Simulation simulation) {
        return new ThresholdBoarding();
    }

    @Override
    public TravelStrategy createTravelStrategy(Simulation simulation) {
        //FIXME
        List<Integer> floors = new ArrayList<>();
        List<Long> durations = new ArrayList<>();
        int destination;
        int last = -1;
        Building building = simulation.getBuilding();
        Random r = simulation.getBuilding().getSimulation().getRandom();
        int destinations = r.nextInt(4) + 2;

        for (int i = 0; i < destinations; i++){
            destination = r.nextInt(building.getFloorCount() - 1) + 2;
            while(destination == last){
                destination = r.nextInt(building.getFloorCount() - 1) + 2;
            }
            floors.add(destination);
            last = destination;
        }

        for (int i = 0; i < destinations; i++){
            durations.add((long)(r.nextGaussian() * 180 + 600));
        }

        return new MultipleDestinationTravel(floors, durations);
    }

    @Override
    public EmbarkingStrategy createEmbarkingStrategy(Simulation simulation) {
        return null;
    }

    @Override
    public DebarkingStrategy createDebarkingStrategy(Simulation simulation) {
        return null;
    }
}
