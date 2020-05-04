package cecs277.passengers;

import cecs277.Simulation;

public class VisitorFactory implements PassengerFactory {
    private Simulation mSimulation;
    private int mFactoryWeight;

    public VisitorFactory(Simulation simulation, int factoryWeight) {
        mFactoryWeight =factoryWeight;
        Passenger p = new Passenger("Visitor", "V", createTravelStrategy(simulation),
                createBoardingStrategy(simulation),
                createEmbarkingStrategy(simulation),
                createDebarkingStrategy(simulation));
    }

    @Override
    public String factoryName() {
        return "Visitor";
    }

    @Override
    public String shortName() {
        return "V";
    }

    @Override
    public int factoryWeight() {
        return mFactoryWeight;
    }

    @Override
    public BoardingStrategy createBoardingStrategy(Simulation simulation) {
        return null;
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
