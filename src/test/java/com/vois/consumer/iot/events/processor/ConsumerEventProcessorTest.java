package com.vois.consumer.iot.events.processor;

import com.vois.consumer.iot.events.dto.ConsumerEvent;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.assertEquals;

@SpringJUnitConfig
@RunWith(SpringJUnit4ClassRunner.class)
public class ConsumerEventProcessorTest {
    private ConsumerEventProcessor processor;

    @Before
    public void setUp() throws Exception {
        processor = new ConsumerEventProcessor();
    }

    @Test
    public void testProcess() throws Exception {
        ConsumerEvent sampleEvent = ConsumerEvent.builder()
                .datetime("1582605197000")
                .eventId("10004")
                .productId("WG11155638")
                .latitude("51.5185")
                .longitude("-0.1736")
                .battery("0.98")
                .light("OFF")
                .airplaneMode("OFF")
                .build();

        ConsumerEvent postProcess = processor.process(sampleEvent);

        // expect nothing has been transform right now.

        assertEquals("10004" , postProcess.getEventId());
        assertEquals("WG11155638" , postProcess.getProductId());
        assertEquals("1582605197000" , postProcess.getDatetime());
        assertEquals("51.5185" , postProcess.getLatitude());
        assertEquals("-0.1736" , postProcess.getLongitude());
        assertEquals("0.98" , postProcess.getBattery());
        assertEquals("OFF" , postProcess.getLight());
        assertEquals("OFF" , postProcess.getAirplaneMode());


    }
}
