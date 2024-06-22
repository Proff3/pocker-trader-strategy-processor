package org.surfer.strategyprocessor.exceptions;

public class OrdersContextInitializationException extends Exception{
    public OrdersContextInitializationException() {
        super("Orders context has not been initialized");
    }
}
