package com.vois.consumer.iot.events.exceptions;

public class NoConsumerEventSourceDataFileFoundException extends Exception {
    public NoConsumerEventSourceDataFileFoundException() {
        super();
    }

    public NoConsumerEventSourceDataFileFoundException(String message) {
        super(message);
    }
}
