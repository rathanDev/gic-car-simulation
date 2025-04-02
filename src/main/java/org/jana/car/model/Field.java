package org.jana.car.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Field {

    private final int x;
    private final int y;
    private final Map<String, Car> initialCarMap;
    private final Map<String, Car> simulatedCarMap;
    private final Map<String, List<Command>> commandMap;
    private final Map<String, Collision> collisionMap;

    public Field(int x, int y) {
        this.x = x;
        this.y = y;
        this.initialCarMap = new HashMap<>();
        this.simulatedCarMap = new HashMap<>();
        this.commandMap = new HashMap<>();
        this.collisionMap = new HashMap<>();
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public Map<String, Car> getInitialCarMap() {
        return initialCarMap;
    }

    public Map<String, Car> getSimulatedCarMap() {
        return simulatedCarMap;
    }

    public Map<String, List<Command>> getCommandMap() {
        return this.commandMap;
    }

    public Map<String, Collision> getCollisionMap() {
        return collisionMap;
    }

}
