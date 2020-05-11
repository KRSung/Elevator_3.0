package cecs277.passengers;

import cecs277.Simulation;
import cecs277.buildings.Building;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class DeliveryPassengerFactory implements PassengerFactory {

    private int mFactoryWeight;

    public DeliveryPassengerFactory (int factoryWeight){
        mFactoryWeight = factoryWeight;
    }

    @Override
    public String factoryName() {
        return "Delivery";
    }

    @Override
    public String shortName() {
        return "D";
    }

    @Override
    public int factoryWeight() {
        return mFactoryWeight;
    }

    @Override
    public BoardingStrategy createBoardingStrategy(Simulation simulation) {
        return new ThresholdBoarding(5);
    }

    @Override
    public TravelStrategy createTravelStrategy(Simulation simulation) {
        Random r = simulation.getRandom();
        List<Integer> floors = new ArrayList<>();
        List<Long> durations = new ArrayList<>();
        int destination;
        int last = -1;
        Building building = simulation.getBuilding();
        int destinations = r.nextInt(building.getFloorCount() * 2 / 3) + 1;

        for (int i = 0; i < destinations; i++){
            destination = r.nextInt(building.getFloorCount() - 1) + 2;
            while(destination == last){
                destination = r.nextInt(building.getFloorCount() - 1) + 2;
            }
            floors.add(destination);
            last = destination;
        }

        for (int i = 0; i < destinations; i++){
            durations.add((long)(r.nextGaussian() * 10 + 60));
        }

        return new MultipleDestinationTravel(floors, durations, simulation);

    }

    @Override
    public EmbarkingStrategy createEmbarkingStrategy(Simulation simulation) {
        return new ResponsibleEmbarking();
    }

    @Override
    public DebarkingStrategy createDebarkingStrategy(Simulation simulation) {
        return new DistractedDebarking();
    }

}
