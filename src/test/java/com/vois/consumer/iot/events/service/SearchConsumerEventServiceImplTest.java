package com.vois.consumer.iot.events.service;

import com.vois.consumer.iot.events.components.EventDataCarrier;
import com.vois.consumer.iot.events.dto.ConsumerEvent;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringJUnitConfig
@RunWith(SpringJUnit4ClassRunner.class)
public class SearchConsumerEventServiceImplTest {

    @Mock
    private EventDataCarrier eventDataCarrier;

    @InjectMocks
    private SearchConsumerEventServiceImpl searchConsumerEventService;

    @Before
    public void setUp() throws Exception {
        searchConsumerEventService = new SearchConsumerEventServiceImpl(eventDataCarrier);
    }

    @Test
    public void testSearchConsumerEventServiceImpl() {
        ConsumerEvent sampleEvent = ConsumerEvent.builder()
                .datetime("1721384606441")
                .eventId("10004")
                .productId("WG11155638")
                .latitude("51.5185")
                .longitude("-0.1736")
                .battery("0.98")
                .light("OFF")
                .airplaneMode("OFF")
                .build();
        when(eventDataCarrier.lookup("WG11155638" , 1721384606441L)).thenReturn(sampleEvent);
        Optional<ConsumerEvent> test = searchConsumerEventService.searchProductEvent("WG11155638" , Optional.of("1721384606441"));
        assertTrue(test.isPresent());
        assertEquals("WG11155638" , test.get().getProductId());
        verify(eventDataCarrier , times(1)).lookup("WG11155638" , 1721384606441L);
    }
}

