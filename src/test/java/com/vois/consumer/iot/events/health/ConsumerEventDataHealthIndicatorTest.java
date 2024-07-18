package com.vois.consumer.iot.events.health;

import com.vois.consumer.iot.events.components.EventDataCarrier;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mock;
import org.springframework.boot.actuate.health.Health;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ConsumerEventDataHealthIndicatorTest {
    @Mock
    private EventDataCarrier eventDataCarrier;

    private ConsumerEventDataHealthIndicator consumerEventDataHealthIndicator;

    @BeforeEach
    public void setUp() {
        consumerEventDataHealthIndicator = new ConsumerEventDataHealthIndicator(eventDataCarrier);
    }

    @Test
    @Ignore
    public void testHealthIndicator() {
        when(eventDataCarrier.getNumberOfRecordsInMemory()).thenReturn(3404983);
        Health health = consumerEventDataHealthIndicator.health();
        verify(eventDataCarrier, times(1)).getNumberOfRecordsInMemory();
        assertTrue(health.getDetails().containsKey("collection_size"));
        assertEquals(3404983, health.getDetails().get("collection_size"));
    }
}
