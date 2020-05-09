package cecs277.passengers;

import cecs277.Simulation;

import java.util.Random;

public class JerkPassengerFactory implements PassengerFactory {
    private int mFactoryWeight;

    public JerkPassengerFactory (int factoryWeight){
        mFactoryWeight = factoryWeight;
    }

    @Override
    public String factoryName() {
        return "Jerk";
    }

    @Override
    public String shortName() {
        return "J";
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
        return new SingleDestinationTravel(r.nextInt(simulation.getBuilding().getFloorCount() - 1) + 2, (int) (r.nextGaussian() * 1200 + 3600 ), simulation);
    }

    @Override
    public EmbarkingStrategy createEmbarkingStrategy(Simulation simulation) {
        return new DisruptiveEmbarking();
    }

    @Override
    public DebarkingStrategy createDebarkingStrategy(Simulation simulation) {
        return new AttentiveDebarking();
    }
}
