package org.jana.car.model;

public enum Direction {
    NORTH('N'),
    EAST('E'),
    SOUTH('S'),
    WEST('W');

    private final char ch;

    Direction(char ch) {
        this.ch = ch;
    }

    public char ch() {
        return ch;
    }

    public static Direction fromChar(char ch) {
        for (Direction direction : Direction.values()) {
            if (direction.ch == ch) {
                return direction;
            }
        }
        throw new IllegalArgumentException("Invalid direction character: " + ch);
    }

    public Direction turnLeft() {
        return values()[(ordinal() + 3) % 4];
    }

    public Direction turnRight() {
        return values()[(ordinal() + 1) % 4];
    }

}
