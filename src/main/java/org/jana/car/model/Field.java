package org.jana.car.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Field {

    private int x;
    private int y;
    private Map<String, Car> carMap;
    private Integer collisionStep;
    private List<String> collidingCarNames;

    public Field(int x, int y) {
        this.x = x;
        this.y = y;
        this.carMap = new HashMap<>();
        collisionStep = null;
        this.collidingCarNames = new ArrayList<>();
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public Map<String, Car> getCarMap() {
        return carMap;
    }

    public Integer getCollisionStep() {
        return collisionStep;
    }

    public void setCollisionStep(Integer collisionStep) {
        this.collisionStep = collisionStep;
    }

    public List<String> getCollidingCarNames() {
        return collidingCarNames;
    }

    public void setCollidingCarNames(List<String> collidingCarNames) {
        this.collidingCarNames = collidingCarNames;
    }

    public void addCar(Car car) {
        this.carMap.put(car.getCarName(), car);
    }

}
