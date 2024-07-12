package com.vois.consumer.iot.events.health;

import com.vois.consumer.iot.events.components.EventDataCarrier;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class ConsumerEventDataHealthIndicator implements HealthIndicator {
    private final EventDataCarrier eventDataCarrier;

    public ConsumerEventDataHealthIndicator(EventDataCarrier eventDataCarrier) {
        this.eventDataCarrier = eventDataCarrier;
    }

    @Override
    public Health health() {
        return Health.up().withDetails(Map.of("collection_size" , eventDataCarrier.getNumberOfRecordsInMemory())).build();
    }
}
