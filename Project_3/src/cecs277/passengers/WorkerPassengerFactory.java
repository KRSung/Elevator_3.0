package cecs277.passengers;

import cecs277.Simulation;
import cecs277.buildings.Building;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class WorkerPassengerFactory implements PassengerFactory {
    public int mFactoryWeight;

    public WorkerPassengerFactory (int factoryWeight){
        this.mFactoryWeight = factoryWeight;
    }

    @Override
    public String factoryName() {
        return "Worker";
    }

    @Override
    public String shortName() {
        return "W";
    }

    public void setFactoryWeight(int factoryWeight){
        mFactoryWeight = factoryWeight;
    }

    @Override
    public int factoryWeight() {
        return mFactoryWeight;
    }

    //FIXME workers board with at most 3 people so i think it should be 4 here
    @Override
    public BoardingStrategy createBoardingStrategy(Simulation simulation) {
        return new ThresholdBoarding(3);
    }

    @Override
    public TravelStrategy createTravelStrategy(Simulation simulation) {
        List<Integer> floors = new ArrayList<>();
        List<Long> durations = new ArrayList<>();
        int destination;
        int last = -1;
        Building building = simulation.getBuilding();
        Random r = simulation.getRandom();
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

        return new MultipleDestinationTravel(floors, durations, simulation);
    }

    @Override
    public EmbarkingStrategy createEmbarkingStrategy(Simulation simulation) {
        return new ResponsibleEmbarking();
    }

    @Override
    public DebarkingStrategy createDebarkingStrategy(Simulation simulation) {
        return new AttentiveDebarking();
    }
}
