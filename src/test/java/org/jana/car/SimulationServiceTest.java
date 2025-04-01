package org.jana.car;

import org.jana.car.model.Car;
import org.jana.car.model.Direction;
import org.jana.car.model.InputXYOfField;
import org.jana.car.model.Position;
import org.jana.car.service.SimulationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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
    void testAddCar() {
        String carNameA = "A";
        assertNull(simulationService.getCar(carNameA));
        Car carA = new Car(carNameA, new Position(1, 2), Direction.N, "FFRFFFFRRL");
        simulationService.addCar(carA);
        assertNotNull(simulationService.getCar(carNameA));
    }

    @Test
    void testIsCarNameExist() {
        String carNameA = "A";
        assertFalse(simulationService.isCarNameExist(carNameA));
        Car carA = new Car(carNameA, new Position(1, 2), Direction.N, "FFRFFFFRRL");
        simulationService.addCar(carA);
        assertTrue(simulationService.isCarNameExist(carNameA));
    }

    @Test
    void testExecutionWithSingleCar() {
        String carNameA = "A";
        Car car = new Car(carNameA, new Position(1, 2), Direction.N, "FFRFFFFRRL");
        simulationService.addCar(car);
        simulationService.execute();

        Position expectedPosition = new Position(5, 4);
        Direction expectedDirection = Direction.S;

        assertEquals(expectedPosition, simulationService.getCar(carNameA).getPosition());
        assertEquals(expectedDirection, simulationService.getCar(carNameA).getDirection());
    }

    @Test
    void testExecutionWithMultipleCars() {
        String carNameA = "A";
        Car car = new Car(carNameA, new Position(1, 2), Direction.N, "FFRFFFFRRL");
        simulationService.addCar(car);

        String carNameB = "B";
        Car carB = new Car(carNameB, new Position(9, 1), Direction.W, "FFFFFFFFFFFFFF");
        simulationService.addCar(carB);

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

        simulationService.execute();

        Position expectedPositionA = new Position(5, 4);
        Direction expectedDirectionA = Direction.S;

        Position expectedPositionB = new Position(0, 1);
        Direction expectedDirectionB = Direction.W;

        assertEquals(expectedPositionA, simulationService.getCar(carNameA).getPosition());
        assertEquals(expectedDirectionA, simulationService.getCar(carNameA).getDirection());

        assertEquals(expectedPositionB, simulationService.getCar(carNameB).getPosition());
        assertEquals(expectedDirectionB, simulationService.getCar(carNameB).getDirection());

        assertTrue(simulationService.getCollidingCarNames().isEmpty());
        assertNull(simulationService.getCollisionStep());
    }

    @Test
    void testExecutionWithCollision() {
        String carNameA = "A";
        Car carA = new Car(carNameA, new Position(1, 2), Direction.N, "FFRFFFFRRL");
        simulationService.addCar(carA);

        String carNameB = "B";
        Car carB = new Car(carNameB, new Position(7, 8), Direction.W, "FFLFFFFFFF");
        simulationService.addCar(carB);

        simulationService.execute();

        Position expectedPosition = new Position(5, 4);

        assertEquals(expectedPosition, simulationService.getCar(carNameA).getPosition());
        assertEquals(expectedPosition, simulationService.getCar(carNameB).getPosition());

        assertEquals(2, simulationService.getCollidingCarNames().size());
        assertEquals(7, simulationService.getCollisionStep());
    }

}
