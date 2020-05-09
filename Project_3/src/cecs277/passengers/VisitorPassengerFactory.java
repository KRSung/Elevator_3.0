package cecs277.passengers;

import cecs277.Simulation;

import java.util.Random;


//FIXME
public class VisitorPassengerFactory implements PassengerFactory {
//    private Simulation mSimulation;
    private int mFactoryWeight;

    public VisitorPassengerFactory(int factoryWeight){
        mFactoryWeight = factoryWeight;
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
