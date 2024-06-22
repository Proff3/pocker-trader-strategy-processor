package org.surfer.strategyprocessor.exceptions;

public class StreamInitializationException extends Exception{
    public StreamInitializationException() {
        super("Stream has not been initialized");
    }
}
