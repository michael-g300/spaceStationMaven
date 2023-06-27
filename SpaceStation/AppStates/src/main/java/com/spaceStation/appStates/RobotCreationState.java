package com.spaceStation.appStates;

import com.spaceStation.appStateInterface.AppState;
import com.spaceStation.fleet.FleetManager;

import java.util.Optional;
import java.util.Scanner;

public class RobotCreationState implements AppState {
    private static final int MODEL_IDX = 0;
    private static final int NAME_IDX = 1;
    private static final int CALL_SIGN_IDX = 2;
    private static final int CREATION_SUCCESS = 1;
    private static final int CREATION_FAIL = 2;
    private static RobotCreationState m_instance = null;
    private RobotCreationState() {m_instance = this;};
    public static final RobotCreationState INSTANCE() {
        if (m_instance == null) {
            m_instance = new RobotCreationState();
        }
        return m_instance;
    }
    @Override
    public AppState Next(int robotCreationResult) {
        return switch (robotCreationResult) {
            case CREATION_FAIL -> RobotCreationState.INSTANCE();
            default -> MainMenuState.INSTANCE();
        };
    }

    @Override
    public int userInteraction(FleetManager fleetManager, Scanner scanner) {
        var sbuilder = new StringBuilder();
        sbuilder.append("Please choose new robots' model from given list and enter name and call sign, separated by a single space:\n");
        sbuilder.append("(1) HAL9000\n");
        sbuilder.append("(2) Tachikomas\n");
        sbuilder.append("(3) Johnny5\n");
        sbuilder.append("(4) Maschinenmensh\n");
        System.out.println(sbuilder);
        return talkToUser(fleetManager, scanner);
    }

    private int talkToUser(FleetManager fleetManager, final Scanner sc) {
        int response = 2;
        final String userInput = sc.nextLine();
        final String[] inputParts = userInput.split(" ");
        if (inputParts.length != 3) {
            System.out.println("Wrong number of parameters. Should be 3, but was " + inputParts.length);
            response = CREATION_FAIL;
        }
        else {
            if (robotModelFromInput(inputParts[MODEL_IDX]).isPresent()) {
                if (robotNameFromInput(inputParts[NAME_IDX]).isPresent()) {
                    if (robotCallSignFromInput(inputParts[CALL_SIGN_IDX]).isPresent()) {
                        fleetManager.createRobot(robotNameFromInput(inputParts[NAME_IDX]).get(), robotCallSignFromInput(inputParts[CALL_SIGN_IDX]).get(), robotModelFromInput(inputParts[MODEL_IDX]).get());
                        response = CREATION_SUCCESS;
                    }
                }
            }
            else {
                System.out.println("Invalid model chosen. Please try again.");
            }
        }
        return response;
    }

    private Optional<String> robotCallSignFromInput(final String callSign) {
        if (callSign.length() < 1) {
            return Optional.empty();
        }
        for (int i = 0 ; i < callSign.length() ; ++i) {
            if (!isAlphaNumeric(callSign.charAt(i))) {
                return Optional.empty();
            }
        }
        return Optional.of(callSign);
    }
    private static boolean isAlphaNumeric(final char char1) {
        return (char1 >= 'a' && char1 <= 'z') || (char1 >= 'A' && char1 <= 'Z') || (char1 >= '0' && char1 <= '9');
    }

    private Optional<String> robotNameFromInput(final String name) {
        if (name.length() < 2 || name.length() > 32) {
            System.out.println("Name should be between 2 and 32 characters long.");
            return Optional.empty();
        }
        return Optional.of(name);
    }

    private Optional<String> robotModelFromInput(final String userChoice) {
        final int modelNum = Integer.parseInt(userChoice);
        return switch (modelNum) {
            case 1 -> Optional.of("HAL9000");
            case 2 -> Optional.of("Tachikomas");
            case 3 -> Optional.of("Johnny5");
            case 4 -> Optional.of("Maschinenmensh");
            default -> Optional.empty();
        };
    }
}
