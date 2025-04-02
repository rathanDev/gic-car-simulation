package org.jana.car.service;

import org.jana.car.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class SimulationService {
    private static final Logger log = LoggerFactory.getLogger(SimulationService.class);

    private Field field;

    public void initField(InputXYOfField input) {
        log.info("InitializeField; x:{} y:{}", input.x(), input.y());
        field = new Field(input.x(), input.y());
    }

    public boolean isCarNameExist(String carName) {
        log.info("IsCarNameUnique; carName:{}", carName);
        return field.getInitialCarMap().containsKey(carName);
    }

    public void addCar(Car car) {
        log.info("AddCar; carName:{}", car.getCarName());
        field.getInitialCarMap().put(car.getCarName(), car);
    }

    public void addCommand(String carName, List<Command> commands) {
        log.info("AddCommand; carName:{} commands:{}", carName, commands);
        field.getCommandMap().put(carName, commands);
    }

    public void runSimulation() {
        log.info("RunSimulation; cars:{}", field.getInitialCarMap().keySet());
        field.getInitialCarMap().forEach((key, value) -> field.getSimulatedCarMap().put(key, new Car(value)));
        int maxSteps = field.getCommandMap().values().stream().mapToInt(List::size).max().orElse(0);
        Map<Position, String> positionMap = field.getSimulatedCarMap().values().stream().collect(Collectors.toMap(Car::getPosition, Car::getCarName));

        int step = 1;
        while (step <= maxSteps) {
            for (Map.Entry<String, Car> entry : field.getSimulatedCarMap().entrySet()) {
                String carName = entry.getKey();
                Car car = entry.getValue();
                var commandOpt = getNextCommand(carName, step);
                if (commandOpt.isEmpty()) continue;
                executeCommand(commandOpt.get(), car);
                if (detectCollision(positionMap, car, step)) return;
                positionMap.put(car.getPosition(), carName);
            }
            step++;
        }
    }

    private Optional<Command> getNextCommand(String carName, int step) {
        List<Command> commands = field.getCommandMap().get(carName);
        if (commands.size() < step) {
            return Optional.empty();
        }
        return Optional.of(commands.get(step - 1));
    }

    private void executeCommand(Command command, Car car) {
        switch (command) {
            case FORWARD -> moveForward(car);
            case LEFT -> car.setDirection(car.getDirection().turnLeft());
            case RIGHT -> car.setDirection(car.getDirection().turnRight());
            default -> log.error("Incompatible command");
        }
    }

    private boolean detectCollision(Map<Position, String> positionMap, Car currentCar, int step) {
        String carName = currentCar.getCarName();
        String carXName = positionMap.get(currentCar.getPosition());
        if (carXName != null && !carXName.equals(carName)) {
            Collision collisionCarA = new Collision(carName, currentCar.getPosition(), step);
            Collision collisionCarX = new Collision(carXName, currentCar.getPosition(), getCollisionStep(carXName, step));
            field.getCollisionMap().put(carName, collisionCarA);
            field.getCollisionMap().put(carXName, collisionCarX);
            return true;
        }
        return false;
    }

    private int getCollisionStep(String carName, int currentStep) {
        int commandsLen = field.getCommandMap().get(carName).size();
        return Math.min(currentStep, commandsLen);
    }

    private void moveForward(Car car) {
        int newX = car.getPosition().getX();
        int newY = car.getPosition().getY();
        Direction direction = car.getDirection();
        switch (direction) {
            case NORTH -> newY++;
            case EAST -> newX++;
            case SOUTH -> newY--;
            case WEST -> newX--;
        }
        if (isWithinBoundaries(newX, newY)) {
            car.setPosition(new Position(newX, newY));
        } else {
            log.warn("Move ignored due to boundary limit");
        }
    }

    public boolean isWithinBoundaries(int x, int y) {
        return x >= 0 && y >= 0 && x < field.getX() && y < field.getY();
    }

    public boolean isWithinField(InputPositionDirection input) {
        int x = input.getPosition().getX();
        int y = input.getPosition().getY();
        return isWithinBoundaries(x, y);
    }

    public Car getSimulatedCar(String carName) {
        log.info("GetSimulatedCar; carName:{}", carName);
        return field.getSimulatedCarMap().get(carName);
    }

    public String printSimulation() {
        StringBuilder sb = new StringBuilder();
        sb.append("\nYour current list of cars are:\n");
        sb.append(printInitialCars());
        sb.append("\nAfter simulation, the result is:\n");
        if (field.getCollisionMap().isEmpty()) {
            sb.append(printSimulatedCars());
        } else {
            sb.append(printCollision());
        }
        return sb.toString();
    }

    public String printCollision() {
        Map<String, Collision> collisionMap = field.getCollisionMap();
        List<Collision> collisions = new ArrayList<>(collisionMap.values());
        String carNameA = collisions.get(0).carName();
        String carNameB = collisions.get(1).carName();
        String positionStr = printPosition(collisionMap.get(carNameA).position());
        return String.format("- %s, collides with %s at %s at step %d\n", carNameA, carNameB, positionStr, collisionMap.get(carNameA).step()) +
                String.format("- %s, collides with %s at %s at step %d\n", carNameB, carNameA, positionStr, collisionMap.get(carNameB).step());
    }

    public String printInitialCars() {
        StringBuilder sb = new StringBuilder();
        field.getInitialCarMap().values().forEach(car -> {
            String commandStr = printCommands(field.getCommandMap().get(car.getCarName()));
            String positionStr = printPosition(car.getPosition());
            sb.append(String.format("- %s, %s %s, %s\n", car.getCarName(), positionStr, car.getDirection().ch(), commandStr));
        });
        return sb.toString();
    }

    public String printSimulatedCars() {
        StringBuilder sb = new StringBuilder();
        field.getSimulatedCarMap().values().forEach(car -> {
            String positionStr = printPosition(car.getPosition());
            sb.append(String.format("- %s, %s %s\n", car.getCarName(), positionStr, car.getDirection().ch()));
        });
        return sb.toString();
    }

    private String printPosition(Position position) {
        return String.format("(%d,%d)", position.getX(), position.getY());
    }

    private String printCommands(List<Command> commands) {
        StringBuilder sb = new StringBuilder();
        commands.forEach(c -> sb.append(c.ch()));
        return sb.toString();
    }

    public List<Collision> getCollisions() {
        return new ArrayList<>(field.getCollisionMap().values());
    }

}
