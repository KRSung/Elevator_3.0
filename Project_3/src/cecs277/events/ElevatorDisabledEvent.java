package cecs277.events;

import cecs277.Simulation;
import cecs277.elevators.DispatchMode;
import cecs277.elevators.Elevator;

public class ElevatorDisabledEvent extends SimulationEvent{
    private Elevator mElevator;
    private DispatchMode mDispatchMode;

    public ElevatorDisabledEvent(long scheduledTime, Elevator elevator, DispatchMode dispatchMode){
        super(scheduledTime);
        mElevator = elevator;
        mDispatchMode = dispatchMode;
    }

    @Override
    public void execute(Simulation sim) {
        mElevator.enable(mDispatchMode);
    }

    @Override
    public String toString() {
        return super.toString() + mElevator;
    }
}
