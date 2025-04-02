package org.jana.car.controller;

import org.jana.car.model.Car;
import org.jana.car.model.InputPositionDirection;
import org.jana.car.service.SimulationService;
import org.jana.car.service.ValidationService;
import org.jana.car.util.InputValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Controller;

import java.util.Scanner;

@Controller
public class SimulationController implements CommandLineRunner {
    private static final Logger log = LoggerFactory.getLogger(SimulationController.class);
    private final Scanner scanner = new Scanner(System.in);

    @Autowired
    private SimulationService simulationService;
    @Autowired
    private ValidationService validator;

    @Override
    public void run(String... args) {
        try {
            startCarSimulation();
        } catch (Exception e) {
            log.error("Err at simulation", e);
        }
    }

    private void startCarSimulation() {
        log.info("Start Auto Driving Car Simulation");
        while (true) {
            System.out.println("\n\nWelcome to Auto Driving Car Simulation!");

            var inputXyOfField = promptForValidInput(
                    "\nPlease enter the width and height of the simulation field in x y format:",
                    validator::validateWidthHeightOfFieldInput);
            simulationService.initField(inputXyOfField);
            System.out.printf("\nYou have created a field of %d x %d.\n", inputXyOfField.x(), inputXyOfField.y());

            while (true) {
                var inputAddCarOrRunSimulation = promptForValidInput(
                        "\nPlease choose from the following options:\n[1] Add a car to field\n[2] Run simulation",
                        validator::validateAddCarOrRunSimulationInput);

                if (inputAddCarOrRunSimulation.addCar()) {
                    addCar();

                } else {
                    simulationService.runSimulation();
                    System.out.println(simulationService.printSimulation());

                    var startOverOrExitOption = promptForValidInput(
                            "\nPlease choose from the following options:\n[1] Start over\n[2] Exit",
                            validator::validateStartOverOrExitInput);
                    if (startOverOrExitOption.startOver()) {
                        break;
                    } else {
                        System.out.println("\nThank you for running the simulation. Goodbye!\n\n");
                        return;
                    }
                }
            }
            log.info("Exit Auto Driving Car Simulation");
        }
    }

    private void addCar() {
        String carName;
        while (true) {
            carName = promptForValidInput("\nPlease enter the name of the car:", validator::validateCarName);
            if (simulationService.isCarNameExist(carName)) {
                System.out.println("Car name already exist! Please try again.");
                continue;
            }
            break;
        }
        InputPositionDirection inputPositionDirection;
        while (true) {
            String positionPromptStr = String.format("\nPlease enter initial position of car %s in x y Direction format:", carName);
            inputPositionDirection = promptForValidInput(positionPromptStr, validator::validateInitialPositionOfCarInput);
            if (!simulationService.isWithinField(inputPositionDirection)) {
                System.out.println("Invalid position! Please try again");
                continue;
            }
            break;
        }
        simulationService.addCar(new Car(carName, inputPositionDirection.getPosition(), inputPositionDirection.getDirection()));
        String commandsPromptStr = String.format("\nPlease enter the commands for car %s:", carName);
        var commands = promptForValidInput(commandsPromptStr, validator::validateCommandsInput);
        simulationService.addCommand(carName, commands);
        System.out.println("\nYour current list of cars are:");
        System.out.println(simulationService.printInitialCars());
    }

    private <T> T promptForValidInput(String message, InputValidator<T> validator) {
        T result;
        do {
            System.out.println(message);
            String input = scanner.nextLine();
            result = validator.validate(input);
        } while (result == null);
        return result;
    }

}
