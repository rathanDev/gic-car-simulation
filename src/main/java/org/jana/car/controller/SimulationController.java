package org.jana.car.controller;

import org.jana.car.model.Car;
import org.jana.car.model.InputPositionDirection;
import org.jana.car.model.Position;
import org.jana.car.service.SimulationService;
import org.jana.car.service.ValidationService;
import org.jana.car.util.InputValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Controller;

import java.util.List;
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
            System.out.printf("\nYou have created a field of %d x %d.\n", inputXyOfField.getX(), inputXyOfField.getY());

            while (true) {
                var inputAddCarOrRunSimulation = promptForValidInput(
                        "\nPlease choose from the following options:\n[1] Add a car to field\n[2] Run simulation",
                        validator::validateAddCarOrRunSimulationInput);

                if (inputAddCarOrRunSimulation.isAddCar()) {
                    addCar();

                } else {
                    runSimulation();

                    var startOverOrExitOption = promptForValidInput(
                            "\nPlease choose from the following options:\n[1] Start over\n[2] Exit",
                            validator::validateStartOverOrExitInput);
                    if (startOverOrExitOption.startOver()) {
                        break;
                    } else {
                        System.out.println("Thank you for running the simulation. Goodbye!\n\n");
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
        String commandsPromptStr = String.format("\nPlease enter the commands for car %s:", carName);
        String commands = promptForValidInput(commandsPromptStr, validator::validateCommandsInput);
        simulationService.addCar(new Car(carName, inputPositionDirection.getPosition(), inputPositionDirection.getDirection(), commands));
        System.out.println("\nYour current list of cars are:");
        System.out.println(simulationService.printCarsWithCommand());
    }

    private void runSimulation() {
        System.out.println("\nYour current list of cars are:");
        System.out.println(simulationService.printCarsWithCommand());
        simulationService.execute();
        System.out.println("After simulation, the result is:");
        List<String> collidingCarNames = simulationService.getCollidingCarNames();
        if (collidingCarNames.isEmpty()) {
            System.out.println(simulationService.printCarsWithoutCommand());
        } else {
            printCollision();
        }
        System.out.println("\n\n");
    }

    private void printCollision() {
        List<String> collidingCarNames = simulationService.getCollidingCarNames();
        String carNameA = collidingCarNames.get(0);
        String carNameB = collidingCarNames.get(1);
        Car carA = simulationService.getCar(carNameA);
        Position position = carA.getPosition();
        Integer collisionStep = simulationService.getCollisionStep();
        String posStr = String.format("(%d, %d)", position.getX(), position.getY());
        System.out.printf("- %s, collides with %s at %s at step %d\n", carNameA, carNameB, posStr, collisionStep);
        System.out.printf("- %s, collides with %s at %s at step %d\n", carNameB, carNameA, posStr, collisionStep);
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
