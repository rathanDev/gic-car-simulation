package org.jana.car.util;

public @FunctionalInterface
interface InputValidator<T> {
    T validate(String input);
}
