package com.spaceStation.robotStates;

import com.spaceStation.robotCommands.RobotCommands;
import com.spaceStation.robotStateInterface.RobotState;

public class WorkingState implements RobotState {
    @Override
    public RobotState Next(RobotCommands command) {
        return switch (command) {
            case END_WORK -> new ActiveState();
            case REBOOT -> new RebootingState();
            default -> this;
        };
    }
}
