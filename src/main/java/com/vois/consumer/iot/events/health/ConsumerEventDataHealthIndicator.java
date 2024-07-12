package com.vois.consumer.iot.events.health;

import com.vois.consumer.iot.events.dto.EventDataCarrier;
import lombok.AllArgsConstructor;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@AllArgsConstructor
public class ConsumerEventDataHealthIndicator implements HealthIndicator {
    private EventDataCarrier eventDataCarrier;

    @Override
    public Health health() {
        return Health.up().withDetails(Map.of("collection_size" , eventDataCarrier.getNumberOfRecordsInMemory())).build();
    }
}
