package org.jana.car.model;

public class Car {

    private final String carName;
    private Position position;
    private Direction direction;

    public Car(String carName, Position position, Direction direction) {
        this.carName = carName;
        this.position = position;
        this.direction = direction;
    }

    public Car(Car other) {
        this.carName = other.carName;
        this.position = new Position(other.position);
        this.direction = other.direction;
    }

    public String getCarName() {
        return carName;
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

}
