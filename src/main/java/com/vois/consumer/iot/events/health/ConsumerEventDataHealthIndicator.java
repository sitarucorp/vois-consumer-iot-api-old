package com.vois.consumer.iot.events.health;

import com.vois.consumer.iot.events.components.EventDataCarrier;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

import java.util.Map;

@Data
@Component
@AllArgsConstructor
public class ConsumerEventDataHealthIndicator implements HealthIndicator {
    private final EventDataCarrier eventDataCarrier;

    @Override
    public Health health() {
        return Health.up().withDetails(Map.of("collection_size" , eventDataCarrier.getNumberOfRecordsInMemory()[0],
                "hold_by",eventDataCarrier.getNumberOfRecordsInMemory()[1] == 0 ? "leader" : "member")).build();
    }
}
