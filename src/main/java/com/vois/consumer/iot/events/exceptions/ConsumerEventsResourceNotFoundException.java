package com.vois.consumer.iot.events.exceptions;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@EqualsAndHashCode(callSuper = true)
@ResponseStatus(value = HttpStatus.NOT_FOUND)
@Data
public class ConsumerEventsResourceNotFoundException extends Exception {
    private String resource;

    public ConsumerEventsResourceNotFoundException() {
        super();
    }

    public ConsumerEventsResourceNotFoundException(String resource) {
        super("Resource not found in the data : productId: " + resource);
        this.resource = resource;
    }
}
