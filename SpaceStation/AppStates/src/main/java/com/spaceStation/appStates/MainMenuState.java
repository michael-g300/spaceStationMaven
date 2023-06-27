package com.spaceStation.appStates;

import com.spaceStation.appStateInterface.AppState;
import com.spaceStation.fleet.FleetManager;

import java.util.Scanner;

public class MainMenuState implements AppState {
    private static final int LIST_ALL_IDX = 1;
    private static final int NEW_ROBOT_IDX = 2;
    private static final int COMMAND_ISSUE_IDX = 3;
    private static final int EXIT_APP_IDX = 4;
    private static MainMenuState m_instance = null;
    private MainMenuState() {m_instance = this;}
    public static MainMenuState INSTANCE() {
        if (m_instance == null) {
            m_instance = new MainMenuState();
        }
        return m_instance;
    }

    @Override
    public AppState Next(int userChoice) {
        return switch (userChoice) {
            case LIST_ALL_IDX -> InventoryState.INSTANCE();
            case NEW_ROBOT_IDX -> RobotCreationState.INSTANCE();
            case COMMAND_ISSUE_IDX -> RobotCommandsMenuState.INSTANCE();
            case EXIT_APP_IDX -> null;
            default -> MainMenuState.INSTANCE();
        };
    }

    @Override
    public int userInteraction(FleetManager fleetManager, Scanner scanner) {
        var sbuilder = new StringBuilder("\n");
        sbuilder.append("(1) List fleet robots\n");
        sbuilder.append("(2) Provision new robot\n");
        sbuilder.append("(3) Issue commands to robot\n");
        sbuilder.append("(4) Quit\n");
        System.out.println(sbuilder);
        return talkToUser(fleetManager, scanner);
    }

    private int talkToUser(FleetManager fleetManager, final Scanner sc) {
        final String userInput = sc.nextLine();
        try {
            final int userChoice = Integer.parseInt(userInput);
            if (userChoice < LIST_ALL_IDX || userChoice > EXIT_APP_IDX) {
                System.out.println("Invalid choice");
            }
            return userChoice;
        }
        catch (NumberFormatException nfe) {
            return -1;
        }
    }
}
