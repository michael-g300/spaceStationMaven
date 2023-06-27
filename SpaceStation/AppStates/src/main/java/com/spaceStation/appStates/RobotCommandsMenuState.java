package com.spaceStation.appStates;

import com.spaceStation.appStateInterface.AppState;
import com.spaceStation.fleet.FleetManager;
import com.spaceStation.robotInterface.Robot;

import java.util.List;
import java.util.Scanner;

public class RobotCommandsMenuState implements AppState {
    private static final String QUIT_MENU = "q";
    private static RobotCommandsMenuState m_instance = null;
    private RobotCommandsMenuState() {m_instance = this;}
    public static RobotCommandsMenuState INSTANCE() {
        if (m_instance == null) {
            m_instance = new RobotCommandsMenuState();
        }
        return m_instance;
    }
    @Override
    public AppState Next(int userChoice) {
        return MainMenuState.INSTANCE();
    }

    @Override
    public int userInteraction(FleetManager fleetManager, final Scanner sc) {
        System.out.println("Choose robot to issue command to (or 'q' to quit):");
        var availableRobots = displayAvailableRobots(fleetManager);
        if (availableRobots.size() == 0) {
            System.out.println("No available robots in fleet at the moment.");
            return 1;
        }
        String userInput = sc.nextLine();
        int userChoice = verifyRobotSelection(userInput, availableRobots);
        while (userChoice == 0) {
            System.out.println("Invalid choice.");
            System.out.println("Choose robot to issue command to (or 'q' to quit):");
            userInput = sc.nextLine();
            userChoice = verifyRobotSelection(userInput, availableRobots);
        }
        if (userChoice == -1) {
            return 1;
        }
        var selectedRobot = availableRobots.get(userChoice - 1);
        System.out.println(displayRobotCommands());
        userInput = sc.nextLine();
        userChoice = verifyCommand(userInput);
        while (userChoice == 0) {
            System.out.println("Invalid choice.");
            userInput = sc.nextLine();
            userChoice = verifyCommand(userInput);
        }
        if (userChoice == -1) {
            return 1;
        }
        executeCommand(userChoice, fleetManager, selectedRobot);
        return 1;
    }

    private void executeCommand(final int userChoice, FleetManager fleetManager, final Robot selectedRobot) {
        switch (userChoice) {
            case 1 -> fleetManager.dispatch(selectedRobot.callSign());
            case 2 -> fleetManager.reboot(selectedRobot.callSign());
            case 3 -> {
                if (selectedRobot.state().getClass().getSimpleName().equals("FailingState")) {
                    fleetManager.selfDiagnose(selectedRobot.callSign());
                }
                else {
                    System.out.println("Unable to perform self-diagnosis on an active robot.");
                }
            }
            case 4 -> fleetManager.deleteRobot(selectedRobot.callSign());
        }
    }

    private int verifyCommand(final String userInput) {
        if (userInput.equals(QUIT_MENU)) {
            return -1;
        }
        try {
            final int commandNum = Integer.parseInt(userInput);
            if (commandNum > 0 && commandNum <= 4) {
                return commandNum;
            }
        }
        catch (NumberFormatException nfe) {
           return 0;
        }
        return 0;
    }

    private StringBuilder displayRobotCommands() {
        var sbuilder = new StringBuilder("(1) Dispatch\n");
        sbuilder.append("(2) Reboot\n");
        sbuilder.append("(3) Self-Diagnostics\n");
        sbuilder.append("(4) Delete\n");
        sbuilder.append("(q) Quit");
        return sbuilder;
    }

    private int verifyRobotSelection(final String userInput, final List<Robot> robots) {
        if (userInput.equals(QUIT_MENU)) {
            return -1;
        }
        try {
            final int robotNum = Integer.parseInt(userInput);
            if (robotNum > 0 && robotNum <= robots.size()) {
                return robotNum;
            }
        }
        catch (NumberFormatException nfe) {
            return 0;
        }
        return 0;
    }

    private List<Robot> displayAvailableRobots(FleetManager fleetManager) {
        var availableRobots = fleetManager.availableRobots();
        if (availableRobots.size() > 0) {
            for (int i = 0 ; i < availableRobots.size() ; ++i) {
                var sbuiler = new StringBuilder("(");
                sbuiler.append(i + 1);
                sbuiler.append(") ");
                sbuiler.append(availableRobots.get(i).toString());
                System.out.println(sbuiler);
            }
        }
        return availableRobots;
    }
}
