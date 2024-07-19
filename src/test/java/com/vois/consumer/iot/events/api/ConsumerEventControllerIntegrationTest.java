package com.vois.consumer.iot.events.api;

import com.vois.consumer.iot.events.dto.ConsumerEvent;
import com.vois.consumer.iot.events.service.DataLoadingServiceImpl;
import com.vois.consumer.iot.events.service.SearchConsumerEventServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Optional;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@WebMvcTest(value = ConsumerEventController.class,
        properties = "version=v2")
@AutoConfigureMockMvc
@Slf4j
@RunWith(SpringJUnit4ClassRunner.class)
public class ConsumerEventControllerIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DataLoadingServiceImpl dataLoadingService;

    @MockBean
    private SearchConsumerEventServiceImpl searchConsumerEventService;


    private ConsumerEventController  consumerEventController;
    @Before
    public void setup() {
        consumerEventController = new ConsumerEventController(dataLoadingService, searchConsumerEventService);
    }

    @Test
    public void testGetVersionInfo() throws Exception {
        String expectedJson = """
                {"version":"v2"}""";

        mockMvc.perform(MockMvcRequestBuilders.get("/version"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string(expectedJson));   // act as assert
    }

    @Test
    public void testGetProductWhenTimestampIsMissingSuccess() throws Exception {
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

        when(searchConsumerEventService.searchProductEvent("WG11155638" , Optional.empty()))
                .thenReturn(Optional.of(sampleEvent));

        String expectedJson = """
                {"id":"WG11155638","name":"CyclePlusTracker","datetime":"25/02/2020  04:33:17","status":"Active","battery":"Full",
                "description":"SUCCESS: Location identified.","long":"-0.1736","lat":"51.5185"}
                """.replaceAll("\n" , "");

        mockMvc.perform(MockMvcRequestBuilders.get("/iot/event/v1").param("ProductId" , "WG11155638"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string(expectedJson));   // act as assert

        verify(searchConsumerEventService , times(1)).
                searchProductEvent("WG11155638" , Optional.empty());
    }

    @Test
    public void testGetProductGenericTrackerProductWhenGeoCoordinatesAvailable() throws Exception {
        ConsumerEvent sampleEvent = ConsumerEvent.builder()
                .datetime("1620701160688")
                .eventId("10004")
                .productId("6911155638")
                .latitude("53.49")
                .longitude("-0.1736")
                .battery("0.98")
                .light("OFF")
                .airplaneMode("OFF")
                .build();

        String expectedJson = """
                {"id":"6911155638","name":"GeneralTracker","datetime":"11/05/2021  02:46:00","status":"Active","battery":"Full",
                "description":"SUCCESS: Location identified.","long":"-0.1736","lat":"53.49"}"""
                .replaceAll("\n" , "");

        when(searchConsumerEventService.searchProductEvent("6911155638" , Optional.of("1620701160688")))
                .thenReturn(Optional.of(sampleEvent));

        mockMvc.perform(MockMvcRequestBuilders.get("/iot/event/v1").param("ProductId" , "6911155638")
                        .queryParam("tstmp" , "1620701160688"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string(expectedJson));   // act as assert

        verify(searchConsumerEventService , times(1)).
                searchProductEvent("6911155638" , Optional.of("1620701160688"));
    }

    @Test
    public void testGetProductGenericTrackerProductWhenAeroplaneModeOn() throws Exception {
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

        String expectedJson = """
                {"id":"6911155638","name":"GeneralTracker","datetime":"11/05/2021  02:46:00","status":"Inactive","battery":"Full",
                "description":"SUCCESS: Location not available: Please turn off airplane mode.","long":"","lat":""}"""
                .replaceAll("\n" , "");

        when(searchConsumerEventService.searchProductEvent("6911155638" , Optional.of("1620701160688")))
                .thenReturn(Optional.of(sampleEvent));

        mockMvc.perform(MockMvcRequestBuilders.get("/iot/event/v1").param("ProductId" , "6911155638")
                        .queryParam("tstmp" , "1620701160688"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string(expectedJson));   // act as assert

        verify(searchConsumerEventService , times(1)).
                searchProductEvent("6911155638" , Optional.of("1620701160688"));
    }


    @Test
    public void testGetProductWhenTimestampIsProvidedSuccess() throws Exception {
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

        when(searchConsumerEventService.searchProductEvent("WG11155638" , Optional.of("1582605197000")))
                .thenReturn(Optional.of(sampleEvent));

        String expectedJson = """
                {"id":"WG11155638","name":"CyclePlusTracker","datetime":"25/02/2020  04:33:17","status":"Active","battery":"Full",
                "description":"SUCCESS: Location identified.","long":"-0.1736","lat":"51.5185"}
                """.replaceAll("\n" , "");

        mockMvc.perform(MockMvcRequestBuilders.get("/iot/event/v1").param("ProductId" , "WG11155638")
                        .queryParam("tstmp" , "1582605197000"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string(expectedJson));   // act as assert

        verify(searchConsumerEventService , times(1)).
                searchProductEvent("WG11155638" , Optional.of("1582605197000"));
    }

    @Test
    public void testGetProductWhenEventHasAirplaneModeOn() throws Exception {
        ConsumerEvent sampleEvent = ConsumerEvent.builder()
                .datetime("1582605197000")
                .eventId("10004")
                .productId("WG11155638")
                .latitude("51.5185")
                .longitude("")
                .battery("0.91")
                .light("OFF")
                .airplaneMode("ON")
                .build();

        when(searchConsumerEventService.searchProductEvent("WG11155638" , Optional.of("1582605197000")))
                .thenReturn(Optional.of(sampleEvent));

        String expectedJson = """
                {"id":"WG11155638","name":"CyclePlusTracker","datetime":"25/02/2020  04:33:17","status":"Inactive","battery":"High",
                "description":"SUCCESS: Location not available: Please turn off airplane mode.","long":"","lat":""}
                """.replaceAll("\n" , "");

        mockMvc.perform(MockMvcRequestBuilders.get("/iot/event/v1").param("ProductId" , "WG11155638")
                        .queryParam("tstmp" , "1582605197000"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string(expectedJson));   // act as assert

        verify(searchConsumerEventService , times(1)).
                searchProductEvent("WG11155638" , Optional.of("1582605197000"));
    }


    @Test
    public void testGetProductWhenEventHasAirplaneModeOffButDeviceCouldNotLocated() throws Exception {
        ConsumerEvent sampleEvent = ConsumerEvent.builder()
                .datetime("1582605197000")
                .eventId("10004")
                .productId("WG11155638")
                .latitude("")
                .longitude("")
                .battery("0.91")
                .light("OFF")
                .airplaneMode("OFF")
                .build();

        when(searchConsumerEventService.searchProductEvent("WG11155638" , Optional.of("1582605197000")))
                .thenReturn(Optional.of(sampleEvent));

        String expectedJson = """
                {"id":"WG11155638","name":"CyclePlusTracker","datetime":"25/02/2020  04:33:17","status":"Inactive","battery":"High",
                "description":"ERROR: Device could not be located","long":"","lat":""}
                """.replaceAll("\n" , "");

        mockMvc.perform(MockMvcRequestBuilders.get("/iot/event/v1").param("ProductId" , "WG11155638")
                        .queryParam("tstmp" , "1582605197000"))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())       // 400 when Device could not be detected
                .andExpect(MockMvcResultMatchers.content().string(expectedJson));   // act as assert

        verify(searchConsumerEventService , times(1)).
                searchProductEvent("WG11155638" , Optional.of("1582605197000"));
    }

    @Test
    public void testGetProductWhenRequiredParametersMissing() throws Exception {
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

        when(searchConsumerEventService.searchProductEvent("WG11155638" , Optional.of("1582605197000")))
                .thenReturn(Optional.of(sampleEvent));

        String expectedJson = """
                {"type":"about:blank","title":
                "Bad Request","status":400,"detail":"Required parameter 'ProductId' is not present.","instance":"/iot/event/v1"}
                """.replaceAll("\n" , "");

        mockMvc.perform(MockMvcRequestBuilders.get("/iot/event/v1"))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError())
                .andExpect(MockMvcResultMatchers.content().string(expectedJson));   // act as assert

        verify(searchConsumerEventService , times(0)).
                searchProductEvent("WG11155638" , Optional.of("1582605197000"));
    }
}
