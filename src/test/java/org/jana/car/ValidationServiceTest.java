package org.jana.car;

import org.jana.car.model.Direction;
import org.jana.car.model.InputXYOfField;
import org.jana.car.service.ValidationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ValidationServiceTest {
    private ValidationService validationService;

    @BeforeEach
    void setUp() {
        validationService = new ValidationService();
    }

    @Test
    void testValidateWidthHeightOfFieldInput() {
        InputXYOfField result = validationService.validateWidthHeightOfFieldInput("10 10");
        assertNotNull(result);
        assertEquals(10, result.x());
        assertEquals(10, result.y());

        result = validationService.validateWidthHeightOfFieldInput("10");
        assertNull(result);

        result = validationService.validateWidthHeightOfFieldInput("abc");
        assertNull(result);

        result = validationService.validateWidthHeightOfFieldInput("-1 10");
        assertNull(result);

        result = validationService.validateWidthHeightOfFieldInput("10 -1");
        assertNull(result);
    }

    @Test
    void testValidateAddCarOrRunSimulationInput() {
        assertNull(validationService.validateAddCarOrRunSimulationInput(null));
        assertNull(validationService.validateAddCarOrRunSimulationInput("10"));
        assertNull(validationService.validateAddCarOrRunSimulationInput("-1"));
        assertNull(validationService.validateAddCarOrRunSimulationInput("0"));
        assertNull(validationService.validateAddCarOrRunSimulationInput("3"));

        var input = validationService.validateAddCarOrRunSimulationInput("1");
        assertTrue(input.addCar());
        assertFalse(input.runSimulation());

        input = validationService.validateAddCarOrRunSimulationInput("2");
        assertFalse(input.addCar());
        assertTrue(input.runSimulation());
    }

    @Test
    void testValidateStartOverOrExitInput() {
        assertNull(validationService.validateStartOverOrExitInput(null));
        assertNull(validationService.validateStartOverOrExitInput("10"));
        assertNull(validationService.validateStartOverOrExitInput("-1"));
        assertNull(validationService.validateStartOverOrExitInput("0"));
        assertNull(validationService.validateStartOverOrExitInput("3"));

        var input = validationService.validateStartOverOrExitInput("1");
        assertTrue(input.startOver());
        assertFalse(input.exit());

        input = validationService.validateStartOverOrExitInput("2");
        assertFalse(input.startOver());
        assertTrue(input.exit());
    }

    @Test
    void testValidateCarName() {
        assertNull(validationService.validateCarName(null));
        assertNull(validationService.validateCarName(" "));
        assertNotNull(validationService.validateCarName("A"));
    }

    @Test
    void testValidateInitialPositionOfCarInput() {
        assertNull(validationService.validateInitialPositionOfCarInput("ABC"));
        assertNull(validationService.validateInitialPositionOfCarInput("1 2"));
        assertNull(validationService.validateInitialPositionOfCarInput("12N"));

        var input = validationService.validateInitialPositionOfCarInput("1 2 N");
        assertEquals(1, input.getPosition().getX());
        assertEquals(2, input.getPosition().getY());
        assertEquals(Direction.NORTH, input.getDirection());
    }

    @Test
    void testValidateCommandsInput() {
        assertNull(validationService.validateCommandsInput("abc"));
        assertNull(validationService.validateCommandsInput("F R L"));
        assertNull(validationService.validateCommandsInput("FRAL"));

        assertNotNull(validationService.validateCommandsInput("FRL"));
        assertNotNull(validationService.validateCommandsInput("RRRL"));
    }

}