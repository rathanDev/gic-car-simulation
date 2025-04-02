package org.jana.car.model;

public enum Command {
    FORWARD('F'),
    RIGHT('R'),
    LEFT('L');

    private final char ch;

    Command(char ch) {
        this.ch = ch;
    }

    public char ch() {
        return ch;
    }

    public static Command fromChar(char ch) {
        for (Command command : Command.values()) {
            if (command.ch == ch) {
                return command;
            }
        }
        return null;
    }

}
