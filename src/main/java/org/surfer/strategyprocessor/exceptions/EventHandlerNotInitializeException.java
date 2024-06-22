package org.surfer.strategyprocessor.exceptions;

public class EventHandlerNotInitializeException extends Exception {
    public EventHandlerNotInitializeException() {
        super("Event handler has not been initialized");
    }
}