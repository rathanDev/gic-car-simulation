package org.jana.car.model;

public class Car {

    private String carName;
    private Position position;
    private Direction direction;
    private String command;

    public Car(String carName, Position position, Direction direction, String command) {
        this.carName = carName;
        this.position = position;
        this.direction = direction;
        this.command = command;
    }

    public String getCarName() {
        return carName;
    }

    public void setCarName(String carName) {
        this.carName = carName;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public Direction getDirection() {
        return direction;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

}
