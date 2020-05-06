package cecs277.passengers;

import cecs277.Simulation;

public class WorkerFactory implements PassengerFactory {
    public int mFactoryWeight;
    public WorkerFactory (Simulation simulation, int factoryWeight){
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
        return null;
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
