package org.surfer.strategyprocessor.exceptions;

public class NoMarketInstrumentException extends Exception {
    public NoMarketInstrumentException() {
        super("No marketInstrument found");
    }
}
