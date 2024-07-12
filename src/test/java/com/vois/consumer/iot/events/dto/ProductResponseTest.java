package com.vois.consumer.iot.events.dto;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static junit.framework.Assert.assertEquals;

@Slf4j
@SpringJUnitConfig
@RunWith(SpringJUnit4ClassRunner.class)
public class ProductResponseTest {

    @Test
    @SneakyThrows
    public void testToProductResponse() throws JsonProcessingException {
        ConsumerEvent consumerEvent = new ConsumerEvent.ConsumerEventBuilder()
                .datetime("1582605197000")
                .eventId("10004")
                .productId("WG11155638")
                .latitude("51.5185")
                .longitude("-0.1736")
                .battery("0.98")
                .light("OFF")
                .airplaneMode("OFF")
                .build();

        ProductResponse productResponse = ProductResponse.
                toProductResponse(consumerEvent);
        log.info("case 1: input {}", consumerEvent);
        log.info("case 1: output {}", productResponse);

        assertEquals("WG11155638", productResponse.getId());
        assertEquals("CyclePlusTracker", productResponse.getName());
        assertEquals("25/02/2020  04:33:17", productResponse.getDatetime());
        assertEquals("Active", productResponse.getStatus());
        assertEquals("Full", productResponse.getBattery());
        assertEquals("SUCCESS: Location identified.", productResponse.getDescription());

        String expectedJson = """
                {"id":"WG11155638","name":"CyclePlusTracker","datetime":"25/02/2020  04:33:17","status":"Active","battery":"Full",
                "description":"SUCCESS: Location identified.","long":"-0.1736","lat":"51.5185"}""".replaceAll("\n", "");

        log.info("case 1 : json => {}", expectedJson);
        assertEquals(expectedJson, new ObjectMapper().writeValueAsString(productResponse));
    }

    @Test
    @SneakyThrows
    public void testToProductResponseInactive() throws JsonProcessingException {
        ConsumerEvent consumerEvent = new ConsumerEvent.ConsumerEventBuilder()
                .datetime("158260528300")
                .eventId("10004")
                .productId("6911155638")
                .latitude("")
                .longitude("-0.1736")
                .battery("0.97")
                .light("OFF")
                .airplaneMode("OFF")
                .build();

        ProductResponse productResponse = ProductResponse.
                toProductResponse(consumerEvent);
        log.info("case 2: input {}", consumerEvent);
        log.info("case 2: output {}", productResponse);

        assertEquals("6911155638", productResponse.getId());
        assertEquals("GeneralTracker", productResponse.getName());
        assertEquals("06/01/1975  17:15:28", productResponse.getDatetime());
        assertEquals("Inactive", productResponse.getStatus());
        assertEquals("High", productResponse.getBattery());
        assertEquals("ERROR: Device could not be located", productResponse.getDescription());

        String expectedJson = """
                {"id":"6911155638","name":"GeneralTracker","datetime":"06/01/1975  17:15:28",
                "status":"Inactive","battery":"High","description":"ERROR: Device could not be located",
                "long":"-0.1736","lat":""}""".replaceAll("\n", "");

        log.info("case 2 : json => {}", expectedJson);
        assertEquals(expectedJson, new ObjectMapper().writeValueAsString(productResponse));
    }

    @Test
    @SneakyThrows
    public void testToProductResponseButAirplaneModeIsOn() throws JsonProcessingException {
        ConsumerEvent consumerEvent = new ConsumerEvent.ConsumerEventBuilder()
                .datetime("158260528300")
                .eventId("10004")
                .productId("6911155638")
                .latitude("-85.13")
                .longitude("-0.1736")
                .battery("0.97")
                .light("OFF")
                .airplaneMode("ON")
                .build();

        ProductResponse productResponse = ProductResponse.
                toProductResponse(consumerEvent);

        log.info("case 2: input {}", consumerEvent);
        log.info("case 2: output {}", productResponse);

        assertEquals("6911155638", productResponse.getId());
        assertEquals("GeneralTracker", productResponse.getName());
        assertEquals("06/01/1975  17:15:28", productResponse.getDatetime());
        assertEquals("Inactive", productResponse.getStatus());
        assertEquals("High", productResponse.getBattery());
        assertEquals("SUCCESS: Location not available: Please turn off airplane mode.", productResponse.getDescription());

        // checks
        // 1. latitude and longitude information has been left blank here.
        // 2. priority has been given to the airplane mode over the information available related to geo-coordinates.

        String expectedJson = """
                {"id":"6911155638","name":"GeneralTracker","datetime":"06/01/1975  17:15:28","status":"Inactive",
                "battery":"High","description":"SUCCESS: Location not available: Please turn off airplane mode.","long":"","lat":""}"""
                .replaceAll("\n", "");

        log.info("case 2 : json => {}", expectedJson);
        assertEquals(expectedJson, new ObjectMapper().writeValueAsString(productResponse));
    }

}
