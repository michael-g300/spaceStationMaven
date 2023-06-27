package com.spaceStation.fleet;

import com.spaceStation.robotInterface.Robot;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class RobotFleet {
    private final Map<String, Robot> m_robots;
    public RobotFleet() {
        m_robots = new HashMap<>();
    }
    public boolean add(final Robot newRobot) {
        if (m_robots.containsKey(newRobot.callSign())) {
            return false;
        }
        m_robots.put(newRobot.callSign(), newRobot);
        return true;
    }
    public boolean delete(final String callSign) {
        if (m_robots.containsKey(callSign)) {
            m_robots.remove(callSign);
            return true;
        }
        return false;
    }
    public Optional<Robot> get(final String callSign) {
        if (m_robots.containsKey(callSign)) {
            return Optional.of(m_robots.get(callSign));
        }
        return Optional.empty();
    }
    public List<Robot> getAll() {
        return m_robots.values().stream().toList();
    }
}
