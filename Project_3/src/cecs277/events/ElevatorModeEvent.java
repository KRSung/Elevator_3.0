package cecs277.events;

import cecs277.Simulation;
import cecs277.elevators.Elevator;
import cecs277.elevators.OperationMode;

public class ElevatorModeEvent extends SimulationEvent{
    private OperationMode mNewMode;
    private Elevator.ElevatorState mNewState;
    private Elevator mElevator;

    public ElevatorModeEvent(long scheduledTime, OperationMode newMode, Elevator.ElevatorState newState, Elevator elevator) {
        super(scheduledTime);
        mNewMode = newMode;
        mElevator = elevator;
        mNewState = newState;
        setPriority(1);
    }

    @Override
    public void execute(Simulation sim) {
        mElevator.setOpMode(mNewMode);
        mElevator.setState(mNewState);
        mElevator.tick();
    }

    @Override
    public String toString() {
        return super.toString() + mElevator;
    }
}
