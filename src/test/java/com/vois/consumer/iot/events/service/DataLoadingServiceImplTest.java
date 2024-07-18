package com.vois.consumer.iot.events.service;

import com.vois.consumer.iot.events.components.EventDataCarrier;
import com.vois.consumer.iot.events.dto.ConsumerEvent;
import com.vois.consumer.iot.events.exceptions.NoConsumerEventSourceDataFileFoundException;
import lombok.SneakyThrows;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.StreamCorruptedException;
import java.util.concurrent.ConcurrentHashMap;

import static org.junit.Assert.assertEquals;

@SpringJUnitConfig
@RunWith(SpringJUnit4ClassRunner.class)
public class DataLoadingServiceImplTest {
    @Test
    @SneakyThrows
    public void testConversion() throws StreamCorruptedException {
        ConsumerEvent event = DataLoadingServiceImpl.convert("1582605137000,10002,WG11155638,51.5185,-0.1736,0.99,OFF,OFF");
        assertEquals("1582605137000", event.getDatetime());
        assertEquals("10002", event.getEventId());
        assertEquals("WG11155638", event.getProductId());
        assertEquals("51.5185", event.getLatitude());
        assertEquals("-0.1736", event.getLongitude());
        assertEquals("0.99", event.getBattery());
        assertEquals("OFF", event.getLight());
        assertEquals("OFF", event.getAirplaneMode());
    }

    @Test(expected = NoConsumerEventSourceDataFileFoundException.class)
    @SneakyThrows
    public void testValidateIfFileExists() throws NoConsumerEventSourceDataFileFoundException {
        DataLoadingServiceImpl dataLoadingService = new DataLoadingServiceImpl(
                new EventDataCarrier(new ConcurrentHashMap<>(), new ConcurrentHashMap<>()));
        dataLoadingService.validateIfFileExists("/opt/abcd/idontknow/tatafoofoo/path.txt");
    }
}
