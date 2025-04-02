package org.jana.car;

import org.jana.car.model.*;
import org.jana.car.service.SimulationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class SimulationServiceTest {

    private SimulationService simulationService;

    @BeforeEach
    void setUp() {
        simulationService = new SimulationService();
        simulationService.initField(new InputXYOfField(10, 10));
    }

    @Test
    void testInitFieldIsWithinBoundaries() {
        assertFalse(simulationService.isWithinBoundaries(10, 10));
        assertTrue(simulationService.isWithinBoundaries(9, 9));
    }

    @Test
    void testIsCarNameExist() {
        String carNameA = "A";
        assertFalse(simulationService.isCarNameExist(carNameA));
        Car carA = new Car(carNameA, new Position(1, 2), Direction.NORTH);
        simulationService.addCar(carA);
        simulationService.addCommand(carNameA, getCommands("FFRFFFFRRL"));
        assertTrue(simulationService.isCarNameExist(carNameA));
    }

    @Test
    void testExecutionWithSingleCar() {
        String carNameA = "A";
        Car car = new Car(carNameA, new Position(1, 2), Direction.NORTH);
        simulationService.addCar(car);
        simulationService.addCommand(carNameA, getCommands("FFRFFFFRRL"));
        simulationService.runSimulation();

        Position expectedPosition = new Position(5, 4);
        Direction expectedDirection = Direction.SOUTH;

        assertEquals(expectedPosition, simulationService.getSimulatedCar(carNameA).getPosition());
        assertEquals(expectedDirection, simulationService.getSimulatedCar(carNameA).getDirection());
    }

    @Test
    void testPrintSimulationWithSingleCar() {
        String carNameA = "A";
        Car car = new Car(carNameA, new Position(1, 2), Direction.NORTH);
        simulationService.addCar(car);
        simulationService.addCommand(carNameA, getCommands("FFRFFFFRRL"));
        simulationService.runSimulation();
        String actualPrint = simulationService.printSimulation();
        String expectedPrint =
                "\nYour current list of cars are:\n" +
                        "- A, (1,2) N, FFRFFFFRRL\n" +
                        "\n" +
                        "After simulation, the result is:\n" +
                        "- A, (5,4) S\n";
        assertEquals(expectedPrint.trim(), actualPrint.trim());
    }


    @Test
    void testExecutionWithMultipleCars() {
        String carNameA = "A";
        Car car = new Car(carNameA, new Position(1, 2), Direction.NORTH);
        simulationService.addCar(car);
        simulationService.addCommand(carNameA, getCommands("FFRFFFFRRL"));

        String carNameB = "B";
        Car carB = new Car(carNameB, new Position(9, 1), Direction.WEST);
        simulationService.addCar(carB);
        simulationService.addCommand(carNameB, getCommands("FFFFFFFFFFFFFF"));

        /*
        9
        8
        7
        6
  y     5
        4   ^   >   >   >   V
        3   ^
        2   ^
        1   <...................................<
        0
            0   1   2   3   4   5   6   7   8   9
                        x
        * */

        simulationService.runSimulation();

        Position expectedPositionA = new Position(5, 4);
        Direction expectedDirectionA = Direction.SOUTH;

        Position expectedPositionB = new Position(0, 1);
        Direction expectedDirectionB = Direction.WEST;

        assertEquals(expectedPositionA, simulationService.getSimulatedCar(carNameA).getPosition());
        assertEquals(expectedDirectionA, simulationService.getSimulatedCar(carNameA).getDirection());

        assertEquals(expectedPositionB, simulationService.getSimulatedCar(carNameB).getPosition());
        assertEquals(expectedDirectionB, simulationService.getSimulatedCar(carNameB).getDirection());

        assertTrue(simulationService.getCollisions().isEmpty());
    }

    @Test
    void testExecutionWithCollision() {
        String carNameA = "A";
        Car carA = new Car(carNameA, new Position(1, 2), Direction.NORTH);
        simulationService.addCar(carA);
        simulationService.addCommand(carNameA, getCommands("FFRFFFFRRL"));

        String carNameB = "B";
        Car carB = new Car(carNameB, new Position(7, 8), Direction.WEST);
        simulationService.addCar(carB);
        simulationService.addCommand(carNameB, getCommands("FFLFFFFFFF"));

        simulationService.runSimulation();

        Position expectedPosition = new Position(5, 4);

        assertEquals(expectedPosition, simulationService.getSimulatedCar(carNameA).getPosition());
        assertEquals(expectedPosition, simulationService.getSimulatedCar(carNameB).getPosition());

        assertEquals(2, simulationService.getCollisions().size());
        assertEquals(7, simulationService.getCollisions().get(0).step());
    }

    @Test
    void testPrintSimulationWithCollision() {
        String carNameA = "A";
        Car carA = new Car(carNameA, new Position(1, 2), Direction.NORTH);
        simulationService.addCar(carA);
        simulationService.addCommand(carNameA, getCommands("FFRFFFFRRL"));

        String carNameB = "B";
        Car carB = new Car(carNameB, new Position(7, 8), Direction.WEST);
        simulationService.addCar(carB);
        simulationService.addCommand(carNameB, getCommands("FFLFFFFFFF"));

        simulationService.runSimulation();
        String actualPrint = simulationService.printSimulation();

        String expectedPrint =
                "\nYour current list of cars are:\n" +
                        "- A, (1,2) N, FFRFFFFRRL\n" +
                        "- B, (7,8) W, FFLFFFFFFF\n" +
                        "\nAfter simulation, the result is:\n" +
                        "- A, collides with B at (5,4) at step 7\n" +
                        "- B, collides with A at (5,4) at step 7\n";
        assertEquals(expectedPrint, actualPrint);
    }

    private List<Command> getCommands(String commandStr) {
        return commandStr.chars().mapToObj(c -> Command.fromChar((char) c)).toList();
    }

}
