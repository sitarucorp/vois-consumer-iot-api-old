package com.vois.consumer.iot.events.api;

import com.vois.consumer.iot.events.dto.LoadConsumerEventRequest;
import com.vois.consumer.iot.events.dto.LoadConsumerEventResponse;
import com.vois.consumer.iot.events.exceptions.ConsumerEventsApplicationException;
import com.vois.consumer.iot.events.exceptions.ConsumerEventsResourceNotFoundException;
import com.vois.consumer.iot.events.exceptions.NoConsumerEventSourceDataFileFoundException;
import com.vois.consumer.iot.events.service.DataLoadingServiceImpl;
import com.vois.consumer.iot.events.service.SearchConsumerEventServiceImpl;
import jakarta.servlet.http.HttpServletResponseWrapper;
import lombok.SneakyThrows;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Optional;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringJUnitConfig
@RunWith(SpringJUnit4ClassRunner.class)
public class ConsumerEventControllerTest {

    @Mock
    private DataLoadingServiceImpl dataLoadingService;
    @Mock
    private SearchConsumerEventServiceImpl searchConsumerEventService;

    private ConsumerEventController consumerEventController;

    private HttpServletResponseWrapper response;

    @Before
    public void setup() {
        consumerEventController = new ConsumerEventController(dataLoadingService , searchConsumerEventService);
        response = new HttpServletResponseWrapper(new MockHttpServletResponse());
    }

    @Test
    @SneakyThrows
    public void testGetProductByProductId() {
        when(searchConsumerEventService.searchProductEvent("FOO19238483" , Optional.empty()));
        try {
            consumerEventController.getProduct("FOO19238483" , null);
            assertTrue(false);
        } catch (ConsumerEventsResourceNotFoundException e) {
            assertTrue(true);
        }
        verify(searchConsumerEventService , times(1)).searchProductEvent("FOO19238483" , Optional.empty());
    }

    @Test
    @SneakyThrows
    public void testLoadConsumerEvent() {
        try {
            when(dataLoadingService.refreshDataFromFile(anyString())).thenReturn(Optional.of(100));
            ResponseEntity<LoadConsumerEventResponse> response = consumerEventController.loadConsumerEvent(
                    LoadConsumerEventRequest.builder().filepath("/tmp/foo.csv").build());
            assertEquals(200 , response.getStatusCode().value());
            assertEquals("data refreshed" , response.getBody().getDescription());
        } catch (NoConsumerEventSourceDataFileFoundException | ConsumerEventsApplicationException e) {
            assertFalse(true);
        }
        verify(dataLoadingService , times(1)).refreshDataFromFile(anyString());
        verify(dataLoadingService , times(0)).loadDataFromFile(anyString());
    }
}
