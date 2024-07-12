package com.vois.consumer.iot.events.util;

import com.vois.consumer.iot.events.dto.ConsumerEvent;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Random;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

@Slf4j
public class IotEventUtilTest {

    @Test
    public void testConvertMillisecondsToVoisReadableFormat() {
        // as per the document provided the json response shows two space characters.
        assertNotEquals("25/02/2020 04:31:17" , IotEventUtil.convertMillisecondsToVoisReadableFormat(1582605077000L));
        assertEquals("25/02/2020  04:31:17" , IotEventUtil.convertMillisecondsToVoisReadableFormat(1582605077000L));
        assertEquals("25/02/2020  04:31:17" , IotEventUtil.convertMillisecondsToVoisReadableFormat(Instant.ofEpochMilli(1582605077000L)));

        long val = LocalDateTime.of(2020 , 2 , 25 , 4 , 31 , 17).toInstant(ZoneOffset.UTC).toEpochMilli();
        assertEquals("25/02/2020  04:31:17" , IotEventUtil.convertMillisecondsToVoisReadableFormat(val));
    }

    @Test
    public void testGetStatusByLatLongReturnActiveWhenLatLongPresent() {
        assertEquals("Active" , IotEventUtil.getStatusByLatLong("52.48" , "-73.24", false));
        assertEquals("Active" , IotEventUtil.getStatusByLatLong("40.73061" , "-73.935242", false));
    }

    @Test
    public void testGetStatusByLatLongReturnInactiveWhenLatLongAbsent() {
        assertEquals("Inactive" , IotEventUtil.getStatusByLatLong("" , "", false));
        assertEquals("Inactive" , IotEventUtil.getStatusByLatLong(null , null, false));
        assertEquals("Inactive" , IotEventUtil.getStatusByLatLong("40.73061" , null, false));
        assertEquals("Inactive" , IotEventUtil.getStatusByLatLong(null , "-73.935242", false));
    }

    @Test
    public void testFindNearestTimestampEventId() {
        Map<String, String> samples = Map.of("6900012" , "1582605077000" , "6900013" , "1582605137000" , "6900014" ,
                "1582405217000" , "6900015" , "1582205257000" , "6900016" , "1582605257000" , "6900017" ,
                "1582705258000" , "6900018" , "1582805259000" , "6900019" , "1582905317000" , "6900020" ,
                "1582105377000" , "6900021" , "1582605437000");

        assertTrue(IotEventUtil.findNearestTimestampEventId(new LinkedHashMap<>(samples) , 1582305257000L).isPresent());
        assertEquals("6900014" , IotEventUtil.findNearestTimestampEventId(new LinkedHashMap<>(samples) , 1582305257000L).get());

    }

    @Test
    public void testFindNearestTimestampEventIdRandom() {
        Map<String, String> samples = new LinkedHashMap<>();
        Instant current = Instant.now();

        for(int hour = 0; hour < 24; ++hour) {
            String key = String.format("69000%02d" , hour);
            String val = String.valueOf(current.plus(hour , ChronoUnit.HOURS).toEpochMilli());
            samples.put(key , val);
        }
        log.info("Find nearest timezone test case sample map: {}" , samples);
        Random ra = new Random();

        int min = 0;
        int max = 24;

        int randomNumber = ra.nextInt(max - min + 1) + min;
        String expectedClosedTimeNearEpochProvided = String.format("69000%02d" , randomNumber);
        long random = Instant.now().plus(randomNumber , ChronoUnit.HOURS).toEpochMilli();

        assertTrue(IotEventUtil.findNearestTimestampEventId(new LinkedHashMap<>(samples) , random).isPresent());
        assertEquals(expectedClosedTimeNearEpochProvided , IotEventUtil.findNearestTimestampEventId(new LinkedHashMap<>(samples) ,
                random).get());

        log.info("UTC timezone plus provided for randomNumber '{}' generated epoch as '{}', returned eventId as '{}'" ,
                randomNumber , random , IotEventUtil.findNearestTimestampEventId(new LinkedHashMap<>(samples) , random).get());
    }

    @Test
    public void testGetDescriptionMessageOnSuccessfulIdentificationOfEvent(){
        ConsumerEvent sampleEvent = ConsumerEvent.builder()
                .datetime("1620701160688")
                .eventId("10004")
                .productId("6911155638")
                .latitude("")
                .longitude("")
                .battery("0.98")
                .light("OFF")
                .airplaneMode("ON")
                .build();

        assertEquals("SUCCESS: Location not available: Please turn off airplane mode.",
                IotEventUtil.getDescriptionMessageOnSuccessfullIdentificationOfEvent(sampleEvent, true));
    }

}
