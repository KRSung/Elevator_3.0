package cecs277.passengers;

import cecs277.Simulation;

import java.util.Random;


//FIXME
public class VisitorFactory implements PassengerFactory {
//    private Simulation mSimulation;
    private int mFactoryWeight;

    public VisitorFactory(Simulation simulation, int factoryWeight) {
        mFactoryWeight = factoryWeight;
//        mSimulation = simulation;
//        Passenger p = new Passenger("Visitor", "V", createTravelStrategy(simulation),
//                createBoardingStrategy(simulation),
//                createEmbarkingStrategy(simulation),
//                createDebarkingStrategy(simulation));
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
        return new CapacityBoarding();
    }

    @Override
    public TravelStrategy createTravelStrategy(Simulation simulation) {
        Random r = simulation.getRandom();
        return new SingleDestinationTravel(r.nextInt(simulation.getBuilding().getFloorCount() - 1) + 2, (int) (r.nextGaussian() * 1200 + 3600 ), simulation);
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
