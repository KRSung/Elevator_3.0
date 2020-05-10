package cecs277.events;

import cecs277.Simulation;
import cecs277.elevators.Elevator;
import cecs277.elevators.OperationMode;

public class ElevatorModeEvent extends SimulationEvent{
    private OperationMode mNewMode;
    private Elevator mElevator;

    public ElevatorModeEvent(long scheduledTime, OperationMode newMode, Elevator elevator) {
        super(scheduledTime);
        mNewMode = newMode;
        mElevator = elevator;
    }

    @Override
    public void execute(Simulation sim) {
        mElevator.setOpMode(mNewMode);
        mElevator.tick();
    }

    @Override
    public String toString() {
        return super.toString() + mElevator;
    }
}
