package com.spaceStation.robotStates;

import com.spaceStation.robotCommands.RobotCommands;
import com.spaceStation.robotStateInterface.RobotState;

import java.awt.*;

public class ActiveState implements RobotState {
    @Override
    public RobotState Next(RobotCommands command) {
        return switch (command) {
            case START_WORK -> new WorkingState();
            case REBOOT -> new RebootingState();
            default -> this;
        };
    }
}
