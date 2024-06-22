package org.surfer.strategyprocessor.exceptions;

public class WrongStrategyInitializationException extends RuntimeException {
    public WrongStrategyInitializationException() {
        super("Wrong strategy initialization");
    }
}
