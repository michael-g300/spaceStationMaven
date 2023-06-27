package com.spaceStation.robotStates;

import com.spaceStation.robotCommands.RobotCommands;
import com.spaceStation.robotStateInterface.RobotState;

public class FailingState implements RobotState {
    @Override
    public RobotState Next(RobotCommands command) {
        return switch (command) {
            case REBOOT -> new RebootingState();
            default -> this;
        };
    }
}
