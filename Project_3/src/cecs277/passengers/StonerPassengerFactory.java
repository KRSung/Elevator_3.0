package cecs277.passengers;

import cecs277.Simulation;

import java.util.Random;

public class StonerPassengerFactory implements PassengerFactory{
    private int mFactoryWeight;

    public StonerPassengerFactory(int factoryWeight){
        mFactoryWeight = factoryWeight;
    }

    @Override
    public String factoryName() {
        return "Stoner";
    }

    @Override
    public String shortName() {
        return "S";
    }

    @Override
    public int factoryWeight() {
        return mFactoryWeight;
    }

    @Override
    public BoardingStrategy createBoardingStrategy(Simulation simulation) {
        return new CapacityBoarding();
    }

    @Override
    public TravelStrategy createTravelStrategy(Simulation simulation) {
        Random r = simulation.getRandom();
        return new SingleDestinationTravel(r.nextInt(simulation.getBuilding().getFloorCount() - 1)
                + 2, (int) (r.nextGaussian() * 1200 + 3600 ), simulation);
    }

    @Override
    public EmbarkingStrategy createEmbarkingStrategy(Simulation simulation) {
        return new ClumsyEmbarking();
    }

    @Override
    public DebarkingStrategy createDebarkingStrategy(Simulation simulation) {
        return new ConfusedDebarking();
    }
}
