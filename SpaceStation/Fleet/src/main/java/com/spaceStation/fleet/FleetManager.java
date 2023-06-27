package com.spaceStation.fleet;

import com.spaceStation.robotCommands.RobotCommands;
import com.spaceStation.robotInterface.Robot;
import com.spaceStation.robotStates.ActiveState;
import com.spaceStation.robotStates.FailingState;
import com.spaceStation.robotStates.RebootingState;
import com.spaceStation.robots.HAL9000;
import com.spaceStation.robots.Johnny5;
import com.spaceStation.robots.Maschinenmensch;
import com.spaceStation.robots.Tachikomas;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;

public class FleetManager {
    private static final Map<String, BiFunction<String, String, Robot>> m_robotsCreator = new HashMap<>();
    static {
        m_robotsCreator.put(HAL9000.class.getSimpleName(), HAL9000::new);
        m_robotsCreator.put(Johnny5.class.getSimpleName(), Johnny5::new);
        m_robotsCreator.put(Maschinenmensch.class.getSimpleName(), Maschinenmensch::new);
        m_robotsCreator.put(Tachikomas.class.getSimpleName(), Tachikomas::new);
    }
    private static FleetManager m_instance = null;
    private final RobotFleet m_fleet;
    private FleetManager() {
        m_fleet = new RobotFleet();
    }
    public static FleetManager INSTANCE() {
        if (m_instance == null) {
            m_instance = new FleetManager();
        }
        return m_instance;
    }
    public String ListFleet() {
        var robots = m_fleet.getAll();
        var sbuilder = new StringBuilder();
        for (Robot robot : robots) {
            sbuilder.append(robot.toString());
            if (robots.indexOf(robot) < robots.size() - 1) {
                sbuilder.append("\n");
            }
        }
        return sbuilder.toString();
    }
    public void createRobot(final String name, final String callSign, final String model) {
        if (!doesRobotExist(callSign)) {
            Robot newRobot = m_robotsCreator.get(model).apply(name, callSign);
            m_fleet.add(newRobot);
            displayRobotDetails(newRobot);
        }
        else {
            System.out.println("A robot with the callSign " + callSign + " already exists in your fleet. Robot creation failed.");
        }
    }
    public void deleteRobot(final String callSign) {
        if (m_fleet.get(callSign).isPresent()) {
            m_fleet.delete(callSign);
            System.out.println("Robot with callSign " + callSign + " has been deleted from your fleet.");
        }
        else {
            System.out.println("Robot with callSign " + callSign + "does not exist in your fleet.");
        }
    }
    public void dispatch(final String callSign) {
        if (m_fleet.get(callSign).isPresent()) {
            var robot = m_fleet.get(callSign).get();
            if (robot.state() instanceof ActiveState) {
                robot.state().Next(RobotCommands.START_WORK);
                System.out.println("Robot "+ robot.name() + " is in active duty");
                Thread thread = new Thread(robot::work);
                thread.setDaemon(true);
                thread.start();
            }
            else {
                System.out.println("Robot " + robot.name() + " is not active");
            }
        }
        else {
            System.out.println("Robot with callSign " + callSign + "does not exist in your fleet.");
        }
    }
    public void reboot(final String callSign) {
        if (m_fleet.get(callSign).isPresent()) {
            var robot = m_fleet.get(callSign).get();
            System.out.println("Robot "+ robot.name() + " is rebooting");
            robot.setState(new RebootingState());
            Thread thread = new Thread(() -> {
                try {
                    Thread.sleep(new SecureRandom().nextInt(1_000,5_000));
                }
                catch (InterruptedException e) {
                    throw new RuntimeException("Rebooting interrupted. Incomplete.");
                }
                if (!(robot.state() instanceof ActiveState)) {
                    if (robot.state() instanceof FailingState) {
                        robot.SelfDiagnose();
                    }
                    else {
                        robot.setState(new ActiveState());
                    }
                }
            });
            thread.setDaemon(true);
            thread.start();
        }
        else {
            System.out.println("Robot with callSign " + callSign + "does not exist in your fleet.");
        }
    }
    public void selfDiagnose(final String callSign) {
        if (m_fleet.get(callSign).isPresent()) {
            var robot = m_fleet.get(callSign).get();
            if (robot.state() instanceof FailingState) {
                System.out.println("Starting self-diagnose process on robot " + robot.name());
                Thread thread = new Thread(robot::SelfDiagnose);
                thread.setDaemon(true);
                thread.start();
            }
            else {
                System.out.println("Robot " + robot.name() + " is not in a failing state. Invalid operation.");
            }
        }
        else {
            System.out.println("Robot with callSign " + callSign + "does not exist in your fleet.");
        }
    }
    public List<Robot> availableRobots() {
        var robots = m_fleet.getAll();
        var activeOrFailingRobots = new ArrayList<Robot>();
        for (Robot robot : robots) {
            if (robot.state() instanceof ActiveState || robot.state() instanceof FailingState) {
                activeOrFailingRobots.add(robot);
            }
        }
        return activeOrFailingRobots;
    }

    private boolean doesRobotExist(final String callSign) {
        var robots = m_fleet.getAll();
        for (Robot robot : robots) {
            if (robot.callSign().equals(callSign)) {
                return true;
            }
        }
        return false;
    }
    private void displayRobotDetails(final Robot robot) {
        final String output = "The following robot has been successfully added to your fleet:\n" + robot.toString();
        System.out.println(output);
    }
    public void simulateCosmicRays() {
        var robots = m_fleet.getAll();
        for (Robot robot : robots) {
            robot.hit();
        }
    }
}
