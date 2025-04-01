package org.jana.car.service;

import org.jana.car.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class SimulationService {
    private static final Logger log = LoggerFactory.getLogger(SimulationService.class);

    private Field field;

    public void initField(InputXYOfField input) {
        int x = input.getX();
        int y = input.getY();
        log.info("InitializeField; x:{} y:{}", x, y);
        field = new Field(x, y);
    }

    public boolean isCarNameExist(String carName) {
        log.info("IsCarNameUnique; carName:{}", carName);
        return field.getCarMap().containsKey(carName);
    }

    public void addCar(Car car) {
        log.info("AddCar; carName:{}", car.getCarName());
        field.addCar(car);
    }

    public Car getCar(String carName) {
        log.info("GetCar; carName:{}", carName);
        return field.getCarMap().get(carName);
    }

    public void execute() {
        log.info("ExecuteSimulation; cars:{}", field.getCarMap().keySet());
        Map<Position, String> positionMap = new HashMap<>();
        List<String> collidingCarNames = Collections.emptyList();
        int step = 1;
        int maxSteps = 0;

        Collection<Car> cars = field.getCarMap().values();
        for (Car car : cars) {
            String carName = car.getCarName();
            positionMap.put(car.getPosition(), carName);
            maxSteps = Math.max(maxSteps, car.getCommand().length());
        }

        while (step <= maxSteps) {
            for (Map.Entry<String, Car> entry : field.getCarMap().entrySet()) {
                String carName = entry.getKey();
                Car car = entry.getValue();
                char command = getNextCommand(car, step);
                if (command == ' ') {
                    continue;
                }
                switch (command) {
                    case 'F' -> moveForward(car);
                    case 'L' -> car.setDirection(car.getDirection().turnLeft());
                    case 'R' -> car.setDirection(car.getDirection().turnRight());
                    default -> log.error("Incompatible command: {}", command);
                }

                String carAtThisPosition = positionMap.get(car.getPosition());
                if (carAtThisPosition != null && !carAtThisPosition.equals(carName)) {
                    field.setCollidingCarNames(Arrays.asList(carAtThisPosition, carName));
                    field.setCollisionStep(step);
                    return;
                }
                positionMap.put(car.getPosition(), carName);
            }
            step++;
        }
    }

    private void moveForward(Car car) {
        int newX = car.getPosition().getX();
        int newY = car.getPosition().getY();
        Direction direction = car.getDirection();

        switch (direction) {
            case N -> newY++;
            case E -> newX++;
            case S -> newY--;
            case W -> newX--;
        }

        if (isWithinBoundaries(newX, newY)) {
            car.setPosition(new Position(newX, newY));
        } else {
            log.warn("Move ignored due to boundary limit");
        }
    }

    private char getNextCommand(Car car, int step) {
        if (step - 1 < car.getCommand().length()) {
            return car.getCommand().charAt(step - 1);
        }
        return ' ';
    }

    public boolean isWithinBoundaries(int x, int y) {
        return x >= 0 && y >= 0 && x < field.getX() && y < field.getY();
    }

    public boolean isWithinField(InputPositionDirection input) {
        int x = input.getPosition().getX();
        int y = input.getPosition().getY();
        return isWithinBoundaries(x, y);
    }

    public List<String> getCollidingCarNames() {
        return field.getCollidingCarNames();
    }

    public Integer getCollisionStep() {
        return field.getCollisionStep();
    }

    public String printCarsWithCommand() {
        return formatCarList(true);
    }

    public String printCarsWithoutCommand() {
        return formatCarList(false);
    }

    private String formatCarList(boolean includeCommands) {
        StringBuilder sb = new StringBuilder();
        field.getCarMap().values().forEach(car -> {
            Position position = car.getPosition();
            sb.append(String.format("- %s, (%d, %d) %s%s\n", car.getCarName(), position.getX(), position.getY(),
                    car.getDirection(), includeCommands ? ", " + car.getCommand() : ""));
        });
        return sb.toString();
    }

}
