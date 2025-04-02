package org.jana.car.model;

public class InputPositionDirection {

    private final Position position;
    private final Direction direction;

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
