package org.jana.car.model;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public enum Command {
    F, R, L;


    public static List<Character> getAsCharList() {
        return Arrays.stream(Command.values()).map(e -> e.name().charAt(0)).collect(Collectors.toList());
    }

}
