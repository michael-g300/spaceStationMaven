package com.spaceStation.toolInterface;

import com.spaceStation.toolStateInterface.ToolState;

public interface Tool {
    void use();
    boolean repair();
    String name();
    void setState(ToolState state);
    boolean isBad();
}
