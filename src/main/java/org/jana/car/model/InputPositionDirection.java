package org.jana.car.model;

public class InputPositionDirection {

    private Position position;
    private Direction direction;

    public InputPositionDirection(int x, int y, Direction direction) {
        this.position = new Position(x, y);
        this.direction = direction;
    }

    public Position getPosition() {
        return position;
    }

    public Direction getDirection() {
        return direction;
    }

}
