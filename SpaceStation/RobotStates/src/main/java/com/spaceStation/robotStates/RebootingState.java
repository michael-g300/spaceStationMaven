package com.spaceStation.robotStates;

import com.spaceStation.robotCommands.RobotCommands;
import com.spaceStation.robotStateInterface.RobotState;

public class RebootingState implements RobotState {
    @Override
    public RobotState Next(RobotCommands command) {
        return switch (command) {
            default -> new ActiveState();
        };
    }
}
