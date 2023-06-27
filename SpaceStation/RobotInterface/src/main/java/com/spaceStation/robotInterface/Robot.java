package com.spaceStation.robotInterface;

import com.spaceStation.robotStateInterface.RobotState;
import com.spaceStation.toolInterface.Tool;

import java.util.List;

public interface Robot {
    void work();
    boolean SelfDiagnose();
    String name();
    String callSign();
    void setState(RobotState state);
    RobotState state();
    boolean hit();
}
