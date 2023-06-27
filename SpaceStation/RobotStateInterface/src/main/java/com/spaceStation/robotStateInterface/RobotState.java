package com.spaceStation.robotStateInterface;

import com.spaceStation.robotCommands.RobotCommands;


public interface RobotState {
    RobotState Next(RobotCommands command);

}
