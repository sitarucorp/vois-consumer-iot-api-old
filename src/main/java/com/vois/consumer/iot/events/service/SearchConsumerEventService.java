package com.vois.consumer.iot.events.service;

import com.vois.consumer.iot.events.dto.ConsumerEvent;

import java.util.Optional;

public interface SearchConsumerEventService {
    Optional<ConsumerEvent> searchProductEvent(String productId , Optional<String> timestamp);
}
