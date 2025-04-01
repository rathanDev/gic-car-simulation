package org.jana.car.service;

import org.jana.car.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ValidationService {
    private static final Logger log = LoggerFactory.getLogger(ValidationService.class);
    private static final String errMessage = "Invalid input! Please try again";

    public InputXYOfField validateWidthHeightOfFieldInput(String inputStr) {
        log.info("ValidateWidthHeightOfFieldInput; inputStr:{}", inputStr);
        if (inputStr == null || inputStr.trim().isEmpty()) {
            System.out.println(errMessage);
            return null;
        }
        String[] splits = inputStr.trim().split(" ");
        int x;
        int y;
        try {
            x = Integer.parseInt(splits[0].trim());
            y = Integer.parseInt(splits[1].trim());
        } catch (Exception e) {
            log.error("Err at validateFieldDimensionInput; inputStr:{}", inputStr, e);
            System.out.println(errMessage);
            return null;
        }
        if (x < 0 || y < 0) {
            System.out.println(errMessage);
            return null;
        }
        return new InputXYOfField(x, y);
    }

    public InputAddCarOrRunSimulation validateAddCarOrRunSimulationInput(String inputStr) {
        log.info("ValidateAddCarOrRunSimulationInput; inputStr:{}", inputStr);
        int option;
        try {
            option = Integer.parseInt(inputStr.trim());
        } catch (Exception e) {
            log.error("Err at validateAddCarOrRunSimulationInput:{}", inputStr, e);
            System.out.println(errMessage);
            return null;
        }
        if (option < 1 || option > 2) {
            System.out.println(errMessage);
            return null;
        }
        return option == 1
                ? new InputAddCarOrRunSimulation(true, false)
                : new InputAddCarOrRunSimulation(false, true);
    }

    public InputStartOverOrExit validateStartOverOrExitInput(String inputStr) {
        log.info("ValidateStartOverOrExitInput; inputStr:{}", inputStr);
        int option;
        try {
            option = Integer.parseInt(inputStr.trim());
        } catch (Exception e) {
            log.error("Err at validateStartOverOrExitInput:{}", inputStr, e);
            System.out.println(errMessage);
            return null;
        }
        if (option < 1 || option > 2) {
            System.out.println(errMessage);
            return null;
        }
        return option == 1
                ? new InputStartOverOrExit(true, false)
                : new InputStartOverOrExit(false, true);
    }

    public String validateCarName(String inputStr) {
        log.info("ValidateCarName; inputStr:{}", inputStr);
        if (inputStr == null || inputStr.trim().isEmpty()) {
            System.out.println(errMessage);
            return null;
        }
        return inputStr.trim();
    }

    public InputPositionDirection validateInitialPositionOfCarInput(String inputStr) {
        log.info("ValidateInitialPositionOfCarInput; inputStr:{}", inputStr);
        if (inputStr == null || inputStr.trim().isEmpty()) {
            System.out.println(errMessage);
            return null;
        }
        String[] splits = inputStr.trim().split(" ");
        int x;
        int y;
        Direction direction;
        try {
            x = Integer.parseInt(splits[0].trim());
            y = Integer.parseInt(splits[1].trim());
            direction = Direction.valueOf(splits[2].trim());
        } catch (Exception e) {
            log.error("Err at validateInitialPositionOfCarInput; input:{}", inputStr, e);
            System.out.println(errMessage);
            return null;
        }
        return new InputPositionDirection(x, y, direction);
    }

    public String validateCommandsInput(String inputStr) {
        log.info("ValidateCommandsInput; inputStr:{}", inputStr);
        if (inputStr == null || inputStr.trim().isEmpty()) {
            System.out.println(errMessage);
            return null;
        }
        String command = inputStr.trim();
        List<Character> commands = Command.getAsCharList();
        for (char c : command.toCharArray()) {
            if (!commands.contains(c)) {
                System.out.println(errMessage);
                return null;
            }
        }
        return command;
    }

}
