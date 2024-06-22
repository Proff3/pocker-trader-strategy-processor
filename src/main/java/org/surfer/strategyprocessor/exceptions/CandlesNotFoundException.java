package org.surfer.strategyprocessor.exceptions;

public class CandlesNotFoundException extends Exception{
    public CandlesNotFoundException() {
        super("Candles not found");
    }
}
