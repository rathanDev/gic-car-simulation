package org.jana.car.model;

public class InputAddCarOrRunSimulation {

    private boolean addCar;
    private boolean runSimulation;

    public InputAddCarOrRunSimulation(boolean addCar, boolean runSimulation) {
        this.addCar = addCar;
        this.runSimulation = runSimulation;
    }

    public boolean isAddCar() {
        return addCar;
    }

    public boolean isRunSimulation() {
        return runSimulation;
    }

}
