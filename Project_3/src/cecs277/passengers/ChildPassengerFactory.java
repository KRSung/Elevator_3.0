package cecs277.passengers;
import cecs277.Simulation;
import java.util.Random;

public class ChildPassengerFactory implements PassengerFactory {

    private int mFactoryWeight;

    public ChildPassengerFactory (int factoryWeight){
        mFactoryWeight = factoryWeight;
    }

    @Override
    public String factoryName() {
        return "Child";
    }

    @Override
    public String shortName() {
        return "C";
    }

    @Override
    public int factoryWeight() {
        return mFactoryWeight;
    }

    @Override
    public BoardingStrategy createBoardingStrategy(Simulation simulation) {
        return new AwkwardBoarding(4);
    }

    @Override
    public TravelStrategy createTravelStrategy(Simulation simulation) {
        Random r = simulation.getRandom();
        return new SingleDestinationTravel(r.nextInt(simulation.getBuilding().getFloorCount() - 1) + 2, (int) (r.nextGaussian() * 1800 + 7200), simulation);
    }

    @Override
    public EmbarkingStrategy createEmbarkingStrategy(Simulation simulation) {
        return new ClumsyEmbarking();
    }

    @Override
    public DebarkingStrategy createDebarkingStrategy(Simulation simulation) {
        return new DistractedDebarking();
    }

}
