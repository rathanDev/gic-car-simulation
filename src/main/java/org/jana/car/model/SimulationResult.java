package org.jana.car.model;

import java.util.List;
import java.util.Map;

public class SimulationResult {

    private Map<String, Car> carMap;
    private Integer step;
    private List<String> collidingCarNames;

    public SimulationResult(Map<String, Car> carMap, List<String> collidingCarNames, Integer step) {
        this.carMap = carMap;
        this.collidingCarNames = collidingCarNames;
        this.step = step;
    }

    public Map<String, Car> getCarMap() {
        return carMap;
    }

    public List<String> getCollidingCarNames() {
        return collidingCarNames;
    }

    public Integer getStep() {
        return step;
    }

}
