package com.vois.consumer.iot.events.health;

import com.vois.consumer.iot.events.components.EventDataCarrier;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.boot.actuate.health.Health;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringJUnitConfig
@RunWith(SpringJUnit4ClassRunner.class)
public class ConsumerEventDataHealthIndicatorTest {
    @Mock
    private EventDataCarrier eventDataCarrier;

    private ConsumerEventDataHealthIndicator consumerEventDataHealthIndicator;

    @Before
    public void setUp() {
        consumerEventDataHealthIndicator = new ConsumerEventDataHealthIndicator(eventDataCarrier);
    }

    @Test
    public void testHealthIndicator() {
        when(eventDataCarrier.getNumberOfRecordsInMemory()).thenReturn(new int[]{3404983, 0});
        Health health = consumerEventDataHealthIndicator.health();
        verify(eventDataCarrier, times(2)).getNumberOfRecordsInMemory();
        assertTrue(health.getDetails().containsKey("collection_size"));
        assertEquals(3404983, health.getDetails().get("collection_size"));
        assertEquals("leader", health.getDetails().get("hold_by"));
    }
}
